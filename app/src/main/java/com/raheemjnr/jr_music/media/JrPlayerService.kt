package com.raheemjnr.jr_music.media

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.os.Build
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.telephony.DataFailCause.NETWORK_FAILURE
import androidx.annotation.RequiresApi
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

private const val MY_MEDIA_ROOT_ID = "jr_root_id"
private const val MY_EMPTY_MEDIA_ROOT_ID = "empty_root_id"

class JrPlayerService : MediaBrowserServiceCompat() {
    //
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var stateBuilder: PlaybackStateCompat.Builder
    private lateinit var notificationManager: MusicNotificationManager

    //music source
    private lateinit var musicSource: MusicSource

    //
    companion object {
        var curSongDuration = 0L
            private set
    }
    private var curPlayingSong: MediaMetadataCompat? = null

    //
    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    //
    private var currentPlaylistItems: List<MediaMetadataCompat> = emptyList()

    private val uAmpAudioAttributes = com.google.android.exoplayer2.audio.AudioAttributes.Builder()
        .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
        .setUsage(C.USAGE_MEDIA)
        .build()

    private lateinit var playerListener: MusicPlayerEventListener
    private var isPlayerInitialized = false

    //
    var isForegroundService = false

    /**
     * Configure ExoPlayer to handle audio focus for us.
     * See [Player.AudioComponent.setAudioAttributes] for details.
     */
    private val currentPlayer: ExoPlayer by lazy {
        ExoPlayer.Builder(this).build().apply {
            setAudioAttributes(uAmpAudioAttributes, true)
            setHandleAudioBecomingNoisy(true)
            addListener(playerListener)
        }
    }


    //
    private lateinit var packageValidator: PackageValidator

    @SuppressLint("RestrictedApi")
    override fun onCreate() {
        super.onCreate()
        // Build a PendingIntent that can be used to launch the UI.
        val sessionActivityPendingIntent =
            packageManager?.getLaunchIntentForPackage(packageName)?.let { sessionIntent ->
                PendingIntent.getActivity(this, 0, sessionIntent, 0)
            }
        // Create a new MediaSession.
        mediaSession = MediaSessionCompat(this, "MusicService")
            .apply {
                setSessionActivity(sessionActivityPendingIntent)
                isActive = true
            }
        /**
         * In order for [MediaBrowserCompat.ConnectionCallback.onConnected] to be called,
         * a [MediaSessionCompat.Token] needs to be set on the [MediaBrowserServiceCompat].
         *
         * It is possible to wait to set the session token, if required for a specific use-case.
         * However, the token *must* be set by the time [MediaBrowserServiceCompat.onGetRoot]
         * returns, or the connection will fail silently. (The system will not even call
         * [MediaBrowserCompat.ConnectionCallback.onConnectionFailed].)
         */
        sessionToken = mediaSession.sessionToken
        /**
         * The notification manager will use our player and media session to decide when to post
         * notifications. When notifications are posted or removed our listener will be called, this
         * allows us to promote the service to foreground (required so that we're not killed if
         * the main UI is not visible).
         */
        notificationManager = MusicNotificationManager(
            this, mediaSession.sessionToken,
            MusicPlayerNotificationListener(this)
        )
        //
        val musicPlaybackPreparer = MediaPlaybackPreparer(musicSource) {
            curPlayingSong = it
            preparePlayer(musicSource.songs, it, true)
        }
    }

    //controls access to the service,
    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {

        /*
        * By default, all known clients are permitted to search, but only tell unknown callers
        * about search if permitted by the [BrowseTree].
        */
        val isKnownCaller = packageValidator.isKnownCaller(clientPackageName, clientUid)


        // (Optional) Control the level of access for the specified package name.
        // You'll need to write your own logic to do this.
        return if (isKnownCaller) {
            // Returns a root ID that clients can use with onLoadChildren() to retrieve
            // the content hierarchy.
            BrowserRoot(MY_MEDIA_ROOT_ID, null)
        } else {
            // Clients can connect, but this BrowserRoot is an empty hierachy
            // so onLoadChildren returns nothing. This disables the ability to browse for content.
            BrowserRoot(MY_EMPTY_MEDIA_ROOT_ID, null)
        }
    }

    /**
    provides the ability for a client to build and
    display a menu of the MediaBrowserService's content hierarchy.
     */
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onLoadChildren(
        parentId: String,
        result: Result<List<MediaBrowserCompat.MediaItem>>
    ) {
        //  Browsing not allowed
        if (MY_EMPTY_MEDIA_ROOT_ID == parentId) {
            result.sendResult(null)
            return
        }

        /**
         * If the caller requests the recent root, return the most recently played song.
         */
        if (parentId == MY_MEDIA_ROOT_ID) {
            // result.sendResult(storage.loadRecentSong()?.let { song -> listOf(song) })
        } else {
            // If the media source is ready, the results will be set synchronously here.
            val resultsSent = musicSource.whenReady { successfullyInitialized ->
                if (successfullyInitialized) {
                    val children = musicSource.songs.map { item ->
                        MediaBrowserCompat.MediaItem(
                            item.description,
                            item.flag
                        )
                    }
                    result.sendResult(children)
                    if (!isPlayerInitialized && musicSource.songs.isNotEmpty()) {
                        preparePlayer(
                            musicSource.songs,
                            musicSource.songs[0],
                            playWhenReady = true,
                        )
                        isPlayerInitialized = true
                    }
                } else {
                    mediaSession.sendSessionEvent(NETWORK_FAILURE.toString(), null)
                    result.sendResult(null)
                }
            }

            // If the results are not ready, the service must "detach" the results before
            // the method returns. After the source is ready, the lambda above will run,
            // and the caller will be notified that the results are ready.
            //
            // See [MediaItemFragmentViewModel.subscriptionCallback] for how this is passed to the
            // UI/displayed in the [RecyclerView].
            if (!resultsSent) {
                result.detach()
            }
        }
    }

    //music queue navigator
    private inner class MusicQueueNavigator(
        mediaSession: MediaSessionCompat
    ) : TimelineQueueNavigator(mediaSession) {
        override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat {
            if (windowIndex < currentPlaylistItems.size) {
                return currentPlaylistItems[windowIndex].description
            }
            return MediaDescriptionCompat.Builder().build()
        }
    }

    /**
     * Load the supplied list of songs and the song to play into the current player.
     */
    private fun preparePlayer(
        metadataList: List<MediaMetadataCompat>,
        itemToPlay: MediaMetadataCompat?,
        playWhenReady: Boolean,
    ) {
        // Since the playlist was probably based on some ordering (such as tracks
        // on an album), find which window index to play first so that the song the
        // user actually wants to hear plays first.
        val initialWindowIndex = if (itemToPlay == null) 0 else metadataList.indexOf(itemToPlay)
        currentPlaylistItems = metadataList

        currentPlayer.playWhenReady = playWhenReady
        currentPlayer.stop()
        // Set playlist and prepare.
        currentPlayer.setMediaItems(
            metadataList.map { it.toMediaItem() }, initialWindowIndex, 0L
        )
        currentPlayer.prepare()
    }

    //
    override fun onDestroy() {
        mediaSession.run {
            isActive = false
            release()
        }

        // Cancel coroutines when the service is going away.
        serviceJob.cancel()

        // Free ExoPlayer resources.
        currentPlayer.removeListener(playerListener)
        currentPlayer.release()
    }

    /**
     * This is the code that causes UAMP to stop playing when swiping the activity away from
     * recents. The choice to do this is app specific. Some apps stop playback, while others allow
     * playback to continue and allow users to stop it with the notification.
     */
//    override fun onTaskRemoved(rootIntent: Intent) {
//        saveRecentSongToStorage()
//        super.onTaskRemoved(rootIntent)
//
//        /**
//         * By stopping playback, the player will transition to [Player.STATE_IDLE] triggering
//         * [Player.EventListener.onPlayerStateChanged] to be called. This will cause the
//         * notification to be hidden and trigger
//         * [PlayerNotificationManager.NotificationListener.onNotificationCancelled] to be called.
//         * The service will then remove itself as a foreground service, and will call
//         * [stopSelf].
//         */
//        currentPlayer.stop(/* reset= */true)
//    }

}