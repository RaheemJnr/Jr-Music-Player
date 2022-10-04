package com.raheemjnr.jr_music.ui.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.database.ContentObserver
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.*
import com.raheemjnr.jr_music.data.model.Songs
import com.raheemjnr.jr_music.media.MusicServiceConnection
import com.raheemjnr.jr_music.media.extentions.id
import com.raheemjnr.jr_music.media.extentions.isPlayEnabled
import com.raheemjnr.jr_music.media.extentions.isPlaying
import com.raheemjnr.jr_music.media.extentions.isPrepared
import com.raheemjnr.jr_music.utils.TAG
import com.raheemjnr.jr_music.utils.loadMusic
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainViewModel(
    application: Application,
    private val musicServiceConnection: MusicServiceConnection
) : AndroidViewModel(application) {
    private val _audio = MutableLiveData<List<Songs>>()
    val audios: LiveData<List<Songs>> get() = _audio

    // collapse value for now playing bottom UI sheet
    val isCollapsed = MutableLiveData(true)

    //contentObserver to fetch local music
    private var contentObserver: ContentObserver? = null

//    val rootMediaId: LiveData<String> =
//
//        Transformations.map(musicServiceConnection.isConnected) { isConnected ->
//            if (isConnected) {
//                musicServiceConnection.roo
//            } else {
//                null
//            }
//        }


    /**
     * Performs a one shot load of audios from [MediaStore.audio.Media.EXTERNAL_CONTENT_URI] into
     * the [_audio] [LiveData] above.
     */
    fun loadAudios() {
        loadMusic(getApplication(), _audio)
    }

    /**
     * Convenience method to convert a day/month/year date into a UNIX timestamp.
     *
     * We're suppressing the lint warning because we're not actually using the date formatter
     * to format the date to display, just to specify a format to use to parse it, and so the
     * locale warning doesn't apply.
     */
    @Suppress("SameParameterValue")
    @SuppressLint("SimpleDateFormat")
    private fun dateToTimestamp(day: Int, month: Int, year: Int): Long =
        SimpleDateFormat("dd.MM.yyyy").let { formatter ->
            TimeUnit.MICROSECONDS.toSeconds(formatter.parse("$day.$month.$year")?.time ?: 0)
        }

    /**
     * This method takes a [Songs] and does one of the following:
     * - If the item is *not* the active item, then play it directly.
     * - If the item *is* the active item, check whether "pause" is a permitted command. If it is,
     *   then pause playback, otherwise send "play" to resume playback.
     */
    fun playMedia(mediaItem: Songs, pauseAllowed: Boolean = true) {
        val nowPlaying = musicServiceConnection.nowPlaying.value
        val transportControls = musicServiceConnection.transportControls

        val isPrepared = musicServiceConnection.playbackState.value?.isPrepared ?: false
        if (isPrepared && mediaItem.id == nowPlaying.id) {
            musicServiceConnection.playbackState.value?.let { playbackState ->
                when {
                    playbackState.isPlaying ->
                        if (pauseAllowed) transportControls.pause() else Unit
                    playbackState.isPlayEnabled -> transportControls.play()
                    else -> {
                        Log.w(
                            TAG, "Playable item clicked but neither play nor pause are enabled!" +
                                    " (mediaId=${mediaItem.id})"
                        )
                    }
                }
            }
        } else {
            transportControls.playFromMediaId(mediaItem.id, null)
        }
    }
    fun playMediaId(mediaId: String) {
        val nowPlaying = musicServiceConnection.nowPlaying.value
        val transportControls = musicServiceConnection.transportControls

        val isPrepared = musicServiceConnection.playbackState.value?.isPrepared ?: false
        if (isPrepared && mediaId == nowPlaying.id) {
            musicServiceConnection.playbackState.value?.let { playbackState ->
                when {
                    playbackState.isPlaying -> transportControls.pause()
                    playbackState.isPlayEnabled -> transportControls.play()
                    else -> {
                        Log.w(
                            TAG, "Playable item clicked but neither play nor pause are enabled!" +
                                    " (mediaId=$mediaId)"
                        )
                    }
                }
            }
        } else {
            transportControls.playFromMediaId(mediaId, null)
        }
    }

    /**
     * Since we register a [ContentObserver], we want to unregister this when the `ViewModel`
     * is being released.
     */
    override fun onCleared() {
        contentObserver?.let {
            getApplication<Application>().contentResolver.unregisterContentObserver(it)
        }
    }

//    class Factory(
//        application: Application,
//        private val musicServiceConnection: MusicServiceConnection
//    ) : ViewModelProvider.NewInstanceFactory() {
//        @Suppress("unchecked_cast")
//        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//            return MainViewModel(application = ,musicServiceConnection) as T
//        }
//    }

    class Factory(
        val app: Application,
        private val musicServiceConnection: MusicServiceConnection
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app, musicServiceConnection) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}



