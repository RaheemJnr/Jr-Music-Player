package com.raheemjnr.jr_music.media

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.telephony.DataFailCause.NETWORK_FAILURE
import androidx.annotation.RequiresApi
import androidx.core.content.PackageManagerCompat.LOG_TAG
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator

private const val MY_MEDIA_ROOT_ID = "jr_root_id"
private const val MY_EMPTY_MEDIA_ROOT_ID = "empty_root_id"

class JrPlayerService : MediaBrowserServiceCompat() {
    //
    private var mediaSession: MediaSessionCompat? = null
    private lateinit var stateBuilder: PlaybackStateCompat.Builder

    //music source
    private lateinit var musicSource: MusicSource

    //
    private var currentPlaylistItems: List<MediaMetadataCompat> = emptyList()

    private val uAmpAudioAttributes = com.google.android.exoplayer2.audio.AudioAttributes.Builder()
        .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
        .setUsage(C.USAGE_MEDIA)
        .build()

    private val playerListener = MusicPlayerEventListener()

    /**
     * Configure ExoPlayer to handle audio focus for us.
     * See [Player.AudioComponent.setAudioAttributes] for details.
     */
    private val currentPlayer: ExoPlayer by lazy {
        SimpleExoPlayer.Builder(this).build().apply {
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

        // Create a MediaSessionCompat
        mediaSession = MediaSessionCompat(baseContext, LOG_TAG).apply {

            // Enable callbacks from MediaButtons and TransportControls
            setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
                        or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
            )

            // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player
            stateBuilder = PlaybackStateCompat.Builder()
                .setActions(
                    PlaybackStateCompat.ACTION_PLAY
                            or PlaybackStateCompat.ACTION_PLAY_PAUSE
                )
            setPlaybackState(stateBuilder.build())

            // MySessionCallback() has methods that handle callbacks from a media controller
            // setCallback(MySessionCallback())

            // Set the session's token so that client activities can communicate with it.
            setSessionToken(sessionToken)
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
                } else {
                    mediaSession?.sendSessionEvent(NETWORK_FAILURE.toString(), null)
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
        playbackStartPositionMs: Long
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
            metadataList.map { it.toMediaItem() }, initialWindowIndex, playbackStartPositionMs
        )
        currentPlayer.prepare()
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