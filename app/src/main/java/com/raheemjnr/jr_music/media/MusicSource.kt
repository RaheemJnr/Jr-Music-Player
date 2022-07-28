package com.raheemjnr.jr_music.media

import android.os.Bundle
import android.support.v4.media.MediaMetadataCompat
import androidx.annotation.IntDef

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

interface MusicSource : Iterable<MediaMetadataCompat> {

    /**
     * Begins loading the data for this music source.
     */
    suspend fun load()


    fun whenReady(performAction: (Boolean) -> Unit): Boolean

    fun search(query: String, extras: Bundle): List<MediaMetadataCompat>
}

@IntDef(
    STATE_CREATED,
    STATE_INITIALIZING,
    STATE_INITIALIZED,
    STATE_ERROR
)

@Retention(AnnotationRetention.SOURCE)
annotation class State

abstract class AbstractMusicSource : MusicSource {
    //
    private val onReadyListeners = mutableListOf<(Boolean) -> Unit>()


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
    override fun whenReady(performAction: (Boolean) -> Unit): Boolean =
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
}



//class MusicSourc {
//
//    private var state: State = STATE_CREATED
//        set(value) {
//            if (value == STATE_INITIALIZED || value == STATE_ERROR) {
//                synchronized(onReadyListener) {
//                    field = value
//                    onReadyListener.forEach { listener ->
//                        listener(state == STATE_INITIALIZED)
//                    }
//                }
//            } else {
//                field = value
//            }
//        }
//    @PlaybackStateCompat.State
//    var state: Int = STATE_CREATED
//        set(value) {
//            if (value == STATE_INITIALIZED || value == STATE_ERROR) {
//                synchronized(onReadyListeners) {
//                    field = value
//                    onReadyListeners.forEach { listener ->
//                        listener(state == STATE_INITIALIZED)
//                    }
//                }
//            } else {
//                field = value
//            }
//        }
//
//}