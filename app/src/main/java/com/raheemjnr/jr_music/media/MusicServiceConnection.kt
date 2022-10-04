package com.raheemjnr.jr_music.media

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.media.MediaBrowserServiceCompat
import com.raheemjnr.jr_music.data.model.Songs
import com.raheemjnr.jr_music.media.MusicServiceConnection.MediaBrowserConnectionCallback
import com.raheemjnr.jr_music.media.extentions.currentPlayBackPosition
import com.raheemjnr.jr_music.media.extentions.id
import kotlinx.coroutines.*

/**
 * Class that manages a connection to a [MediaBrowserServiceCompat] instance, typically a
 * [MusicService] or one of its subclasses.
 *
 * Typically it's best to construct/inject dependencies either using DI or, as UAMP does,
 * using [InjectorUtils] in the app module. There are a few difficulties for that here:
 * - [MediaBrowserCompat] is a final class, so mocking it directly is difficult.
 * - A [MediaBrowserConnectionCallback] is a parameter into the construction of
 *   a [MediaBrowserCompat], and provides callbacks to this class.
 * - [MediaBrowserCompat.ConnectionCallback.onConnected] is the best place to construct
 *   a [MediaControllerCompat] that will be used to control the [MediaSessionCompat].
 *
 *  Because of these reasons, rather than constructing additional classes, this is treated as
 *  a black box (which is why there's very little logic here).
 *
 *  This is also why the parameters to construct a [MusicServiceConnection] are simple
 *  parameters, rather than private properties. They're only required to build the
 *  [MediaBrowserConnectionCallback] and [MediaBrowserCompat] objects.
 */
class MusicServiceConnection(context: Context, private val musicSource: MusicSource) {

    val isConnected = MutableLiveData<Boolean>()
        .apply { postValue(false) }

    //
    val networkFailure: MutableState<Boolean> = mutableStateOf(false)

    //
    val playbackState: MutableState<PlaybackStateCompat?> =
        mutableStateOf(PlaybackStateCompat.fromPlaybackState(null))


    val nowPlaying: MutableState<MediaMetadataCompat> =
        mutableStateOf(MediaMetadataCompat.fromMediaMetadata(NOTHING_PLAYING))

    private val mediaBrowserConnectionCallback = MediaBrowserConnectionCallback(context)
    private val mediaBrowser = MediaBrowserCompat(
        context,
        ComponentName(context, JrPlayerService::class.java),
        mediaBrowserConnectionCallback, null
    ).apply {
        connect()
        updateSong()
    }
    private lateinit var mediaController: MediaControllerCompat

    //
    val transportControls: MediaControllerCompat.TransportControls
        get() = mediaController.transportControls

    //
    fun subscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.subscribe(parentId, callback)
    }

    fun unsubscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.unsubscribe(parentId, callback)
    }

    //
    private val serviceScope = CoroutineScope(Dispatchers.IO)

    //
    val shuffleMode: Int
        get() = mediaController.shuffleMode

    //
    val repeatMode: Int
        get() = mediaController.repeatMode

    //
    val sliderClicked: MutableState<Boolean> = mutableStateOf(false)
    val songDuration: MutableState<Long> = mutableStateOf(0)

    //
    fun updatePlaylist(list: List<Songs>) {
        musicSource.catalogSongs = list
        musicSource.loadMediaData()
    }


    fun updateSong() {
        serviceScope.launch {
            while (!sliderClicked.value) {
                ensureActive()
                delay(100L)
                val pos = playbackState.value?.currentPlayBackPosition
                if (songDuration.value != pos) {
                    pos?.let {
                        songDuration.value = it
                    }
                }
                delay(900L)
            }
        }
    }

    //
    private inner class MediaBrowserConnectionCallback(private val context: Context) :
        MediaBrowserCompat.ConnectionCallback() {
        /**
         * Invoked after [MediaBrowserCompat.connect] when the request has successfully
         * completed.
         */
        override fun onConnected() {
            // Get a MediaController for the MediaSession.
            mediaController = MediaControllerCompat(context, mediaBrowser.sessionToken)
                .apply {
                    registerCallback(MediaControllerCallback())
                }

            isConnected.value = true
        }

        /**
         * Invoked when the client is disconnected from the media browser.
         */
        override fun onConnectionSuspended() {
            isConnected.value = false
        }

        /**
         * Invoked when the connection to the media browser failed.
         */
        override fun onConnectionFailed() {
            isConnected.value = false
        }
    }

    private inner class MediaControllerCallback : MediaControllerCompat.Callback() {

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            playbackState.value = state ?: EMPTY_PLAYBACK_STATE
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            // When ExoPlayer stops we will receive a callback with "empty" metadata. This is a
            // metadata object which has been instantiated with default values. The default value
            // for media ID is null so we assume that if this value is null we are not playing
            // anything.
            nowPlaying.value =
                if (metadata?.id == null) {
                    NOTHING_PLAYING
                } else {
                    metadata
                }

        }


        override fun onQueueChanged(queue: MutableList<MediaSessionCompat.QueueItem>?) {
        }

        override fun onSessionEvent(event: String?, extras: Bundle?) {
            super.onSessionEvent(event, extras)
            when (event) {
                NETWORK_ERROR -> networkFailure.value = true
            }
        }

        /**
         * Normally if a [MediaBrowserServiceCompat] drops its connection the callback comes via
         * [MediaControllerCompat.Callback] (here). But since other connection status events
         * are sent to [MediaBrowserCompat.ConnectionCallback], we catch the disconnect here and
         * send it on to the other callback.
         */
        override fun onSessionDestroyed() {
            mediaBrowserConnectionCallback.onConnectionSuspended()
        }
    }

    companion object {
        // For Singleton instantiation.
        @Volatile
        private var instance: MusicServiceConnection? = null

        fun getInstance(context: Context, musicSource: MusicSource) =
            instance ?: synchronized(this) {
                instance ?: MusicServiceConnection(context, musicSource)
                    .also { instance = it }
            }
    }

}

@Suppress("PropertyName")
val EMPTY_PLAYBACK_STATE: PlaybackStateCompat = PlaybackStateCompat.Builder()
    .setState(PlaybackStateCompat.STATE_NONE, 0, 0f)
    .build()

@Suppress("PropertyName")
val NOTHING_PLAYING: MediaMetadataCompat = MediaMetadataCompat.Builder()
    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, "")
    .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, 0)
    .build()
const val NETWORK_ERROR = "Network Error"
