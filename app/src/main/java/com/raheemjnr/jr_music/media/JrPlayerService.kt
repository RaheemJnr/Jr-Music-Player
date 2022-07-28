package com.raheemjnr.jr_music.media

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.telephony.DataFailCause.NETWORK_FAILURE
import androidx.core.content.PackageManagerCompat.LOG_TAG
import androidx.media.MediaBrowserServiceCompat

private const val MY_MEDIA_ROOT_ID = "jr_root_id"
private const val MY_EMPTY_MEDIA_ROOT_ID = "empty_root_id"

class JrPlayerService : MediaBrowserServiceCompat() {
    //
    private var mediaSession: MediaSessionCompat? = null
    private lateinit var stateBuilder: PlaybackStateCompat.Builder

    //music source
    private lateinit var musicSource: MusicSource


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
            setCallback(MySessionCallback())

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
    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
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
                    val children = browseTree[parentMediaId]?.map { item ->
                        MediaBrowserCompat.MediaItem(item.description, item.flag)
                    }
                    result.sendResult(children)
                } else {
                    mediaSession.sendSessionEvent(NETWORK_FAILURE, null)
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

        when (parentId) {
            MY_MEDIA_ROOT_ID -> {
            }
            else -> {
                val resultsSent = musicSource.whenReady { isInitialized ->
                    if (isInitialized) {
                        val item = musicSource.songs.map { item ->
                            MediaBrowserCompat.MediaItem(
                                item.description,
                                MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
                            )
                        }
                        result.sendResult(item)
                        if (!isPlayerInitialized && musicSource.songs.isNotEmpty()) {
                            preparePlayer(musicSource.songs, musicSource.songs[0], true)
                            isPlayerInitialized = true
                        }
                    } else {
                        mediaSession.sendSessionEvent(NETWORK_ERROR, null)
                        result.sendResult(null)
                    }
                }
                if (!resultsSent) {
                    result.detach()
                }
            }
        }
    }

}