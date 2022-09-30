package com.raheemjnr.jr_music.media

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import androidx.annotation.IntDef
import androidx.lifecycle.MutableLiveData
import com.raheemjnr.jr_music.App
import com.raheemjnr.jr_music.data.model.Songs
import com.raheemjnr.jr_music.media.extentions.*
import com.raheemjnr.jr_music.utils.loadMusic
import java.util.concurrent.TimeUnit

/**
 * State indicating the source was created, but no initialization has performed.
 */
const val STATE_CREATED = 1

/**
 * State indicating initialization of the source is in progress.
 */
const val STATE_INITIALIZING = 2

/**
 * State indicating the source has been initialized and is ready to be used.
 */
const val STATE_INITIALIZED = 3

/**
 * State indicating an error has occurred.
 */
const val STATE_ERROR = 4

@IntDef(
    STATE_CREATED,
    STATE_INITIALIZING,
    STATE_INITIALIZED,
    STATE_ERROR
)

@Retention(AnnotationRetention.SOURCE)
annotation class State

class MusicSource() {

    //
    private val onReadyListeners = mutableListOf<(Boolean) -> Unit>()

    //
    var songs: List<MediaMetadataCompat> = emptyList()

    private fun mapLiveDataToList(songs: List<MediaMetadataCompat>): List<Songs> =
        songs.let {
            it.map { music ->
                Songs(
                    id = music.id ?: "",
                    title = music.title ?: "",
                    album = music.album ?: "",
                    artist = music.artist ?: "",
                    image = music.albumArtUri,
                    duration = music.duration,
                    contentUri = music.mediaUri
                )
            }
        }

    //
    var catalogSongs = loadMusic(
        App.applicationCxt().applicationContext,
        MutableLiveData(mapLiveDataToList(songs))
    )


    fun loadMediaData() {
        state = STATE_INITIALIZED

        songs = catalogSongs?.let { updateCatalog(it) } ?: emptyList()

        state = STATE_INITIALIZED

    }

    @State
    var state: Int = STATE_CREATED
        set(value) {
            if (value == STATE_INITIALIZED || value == STATE_ERROR) {
                synchronized(onReadyListeners) {
                    field = value
                    onReadyListeners.forEach { listener ->
                        listener(state == STATE_INITIALIZED)
                    }
                }
            } else {
                field = value
            }
        }

    /**
     *  /**
     * Method which will perform a given action after this [MusicSource] is ready to be used.
     *
     * @param performAction A lambda expression to be called with a boolean parameter when
     * the source is ready. `true` indicates the source was successfully prepared, `false`
     * indicates an error occurred.
    */
    //     * Performs an action when this MusicSource is ready.
    //     *
    //     * This method is *not* threadsafe. Ensure actions and state changes are only performed
    //     * on a single thread.
    //     */
    fun whenReady(performAction: (Boolean) -> Unit): Boolean =
        when (state) {
            STATE_CREATED, STATE_INITIALIZING -> {
                onReadyListeners += performAction
                false
            }
            else -> {
                performAction(state != STATE_ERROR)
                true
            }
        }

    /**
     * Function to connect to a the local content resolver and fetch MP3file that corresponds to
     * [MediaMetadataCompat] objects.
     */
    fun updateCatalog(catalogSongs: List<Songs>): List<MediaMetadataCompat> {
        val mediaMetadataCompats = catalogSongs.map { songs ->
            MediaMetadataCompat.Builder()
                .from(songs)
                .apply {
                    // Used by ExoPlayer and Notification
                    displayIconUri = songs.image.toString()
                    albumArtUri = songs.image.toString()
                }.build()
        }.toList()
        // Add description keys to be used by the ExoPlayer MediaSession extension when
        // announcing metadata changes.
        mediaMetadataCompats.forEach { it.description.extras?.putAll(it.bundle) }
        return mediaMetadataCompats
    }

    /**
     * Extension method for [MediaMetadataCompat.Builder] to set the fields from
     * our JSON constructed object (to make the code a bit easier to see).
     */
    fun MediaMetadataCompat.Builder.from(localSongs: Songs): MediaMetadataCompat.Builder {
        // The duration from the JSON is given in seconds, but the rest of the code works in
        // milliseconds. Here's where we convert to the proper units.
        val durationMs = TimeUnit.SECONDS.toMillis(localSongs.duration)


        id = localSongs.id
        title = localSongs.title
        artist = localSongs.artist
        album = localSongs.album
        duration = durationMs
        mediaUri = localSongs.contentUri.toString()
        albumArtUri = localSongs.image.toString()
        flag = MediaBrowserCompat.MediaItem.FLAG_PLAYABLE

        // To make things easier for *displaying* these, set the display properties as well.
        displayTitle = localSongs.title
        displaySubtitle = localSongs.artist
        displayDescription = localSongs.album
        displayIconUri = localSongs.image.toString()

        // Add downloadStatus to force the creation of an "extras" bundle in the resulting
        // MediaMetadataCompat object. This is needed to send accurate metadata to the
        // media session during updates.
        downloadStatus = MediaDescriptionCompat.STATUS_NOT_DOWNLOADED

        //Allow it to be used in the typical builder style.

        return this

    }
}