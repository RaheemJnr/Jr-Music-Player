package com.raheemjnr.jr_music.ui.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.database.ContentObserver
import android.provider.MediaStore
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.*
import com.raheemjnr.jr_music.data.model.Songs
import com.raheemjnr.jr_music.media.MusicServiceConnection
import com.raheemjnr.jr_music.utils.Constants.mediaId
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    musicServiceConnection: MusicServiceConnection
) : ViewModel() {
    private val _audio = MutableLiveData<List<Songs>>()
    val audios: LiveData<List<Songs>> get() = _audio

    // collapse value for now playing bottom UI sheet
    val isCollapsed = MutableLiveData(true)

    //contentObserver to fetch local music
    private var contentObserver: ContentObserver? = null

    /**
     * Performs a one shot load of audios from [MediaStore.audio.Media.EXTERNAL_CONTENT_URI] into
     * the [_audio] [LiveData] above.
     */
//    fun loadAudios() {
//        loadMusic(context = BaseApp., _audio)
//    }

    /**
     * Pass the status of the [MusicServiceConnection.networkFailure] through.
     */
    val networkError = Transformations.map(musicServiceConnection.networkFailure) { it }

//    private val subscriptionCallback = object : MediaBrowserCompat.SubscriptionCallback() {
//        override fun onChildrenLoaded(
//            parentId: String,
//            children: List<MediaBrowserCompat.MediaItem>
//        ) {
//        }
//    }

    /**
    //     * When the session's [PlaybackStateCompat] changes, the [mediaItems] need to be updated
    //     * so the correct [MediaItemData.playbackRes] is displayed on the active item.
    //     * (i.e.: play/pause button or blank)
    //     */
//    private val playbackStateObserver = Observer<PlaybackStateCompat> {
//        val playbackState = it ?: EMPTY_PLAYBACK_STATE
//        val metadata = musicServiceConnection.nowPlaying.value ?: NOTHING_PLAYING
//        if (metadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID) != null) {
//            _audio.postValue(updateState(playbackState, metadata))
//        }
//    }
//
//    /**
//     * When the session's [MediaMetadataCompat] changes, the [mediaItems] need to be updated
//     * as it means the currently active item has changed. As a result, the new, and potentially
//     * old item (if there was one), both need to have their [MediaItemData.playbackRes]
//     * changed. (i.e.: play/pause button or blank)
//     */
//    private val mediaMetadataObserver = Observer<MediaMetadataCompat> {
//        val playbackState = musicServiceConnection.playbackState.value ?: EMPTY_PLAYBACK_STATE
//        val metadata = it ?: NOTHING_PLAYING
//        if (metadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID) != null) {
//            _audio.postValue(updateState(playbackState, metadata))
//        }
//    }


    /**
     * Because there's a complex dance between this [ViewModel] and the [MusicServiceConnection]
     * (which is wrapping a [MediaBrowserCompat] object), the usual guidance of using
     * [Transformations] doesn't quite work.
     *
     * Specifically there's three things that are watched that will cause the single piece of
     * [LiveData] exposed from this class to be updated.
     *
     * [subscriptionCallback] (defined above) is called if/when the children of this
     * ViewModel's [mediaId] changes.
     *
     * [MusicServiceConnection.playbackState] changes state based on the playback state of
     * the player, which can change the [MediaItemData.playbackRes]s in the list.
     *
     * [MusicServiceConnection.nowPlaying] changes based on the item that's being played,
     * which can also change the [MediaItemData.playbackRes]s in the list.
     */

//    private val musicServiceConnection = musicServiceConnection.also {
//        it.subscribe(mediaId, subscriptionCallback)
//
//        it.playbackState.observeForever(playbackStateObserver)
//        it.nowPlaying.observeForever(mediaMetadataObserver)
//    }


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


//    private fun updateState(
//        playbackState: PlaybackStateCompat,
//        mediaMetadata: MediaMetadataCompat
//    ): List<Songs> {
//
//        val newResId = when (playbackState.isPlaying) {
//            true -> R.drawable.morevert
//            else -> R.drawable.play_button
//        }
//
//        return _audio.value?.map {
//            val useResId = if (it.id == mediaMetadata.id) newResId else NO_RES
//            it.copy(id = useResId.toString())
//        } ?: emptyList()
//    }
//
//    /**
//     * This method takes a [Songs] and does one of the following:
//     * - If the item is *not* the active item, then play it directly.
//     * - If the item *is* the active item, check whether "pause" is a permitted command. If it is,
//     *   then pause playback, otherwise send "play" to resume playback.
//     */
//    fun playMedia(mediaItem: Songs, pauseAllowed: Boolean = true) {
//        val nowPlaying = musicServiceConnection.nowPlaying.value
//        val transportControls = musicServiceConnection.transportControls
//
//        val isPrepared = musicServiceConnection.playbackState.value?.isPrepared ?: false
//        if (isPrepared && mediaItem.id == nowPlaying?.id) {
//            musicServiceConnection.playbackState.value?.let { playbackState ->
//                when {
//                    playbackState.isPlaying ->
//                        if (pauseAllowed) transportControls.pause() else Unit
//                    playbackState.isPlayEnabled -> transportControls.play()
//                    else -> {
//                        Log.w(
//                            TAG, "Playable item clicked but neither play nor pause are enabled!" +
//                                    " (mediaId=${mediaItem.id})"
//                        )
//                    }
//                }
//            }
//        } else {
//            transportControls.playFromMediaId(mediaItem.id, null)
//        }
//    }
//
//    fun playMediaId(mediaId: String) {
//        val nowPlaying = musicServiceConnection.nowPlaying.value
//        val transportControls = musicServiceConnection.transportControls
//
//        val isPrepared = musicServiceConnection.playbackState.value?.isPrepared ?: false
//        if (isPrepared && mediaId == nowPlaying?.id) {
//            musicServiceConnection.playbackState.value?.let { playbackState ->
//                when {
//                    playbackState.isPlaying -> transportControls.pause()
//                    playbackState.isPlayEnabled -> transportControls.play()
//                    else -> {
//                        Log.w(
//                            TAG, "Playable item clicked but neither play nor pause are enabled!" +
//                                    " (mediaId=$mediaId)"
//                        )
//                    }
//                }
//            }
//        } else {
//            transportControls.playFromMediaId(mediaId, null)
//        }
//    }

    /**
     * Since we register a [ContentObserver], we want to unregister this when the `ViewModel`
     * is being released.
     */
    override fun onCleared() {
//        contentObserver?.let {
//          applicationContext.contentResolver.unregisterContentObserver(it)
//        }

//        // Remove the permanent observers from the MusicServiceConnection.
//        musicServiceConnection.playbackState.removeObserver(playbackStateObserver)
//        musicServiceConnection.nowPlaying.removeObserver(mediaMetadataObserver)
//
//        // And then, finally, unsubscribe the media ID that was being watched.
//        musicServiceConnection.unsubscribe(mediaId, subscriptionCallback)
    }

//    class MainViewmodelFactory(
//        private val musicServiceConnection: MusicServiceConnection
//    ) : ViewModelProvider.Factory {
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
//                @Suppress("UNCHECKED_CAST")
//                return MainViewModel(app, musicServiceConnection) as T
//            }
//            throw IllegalArgumentException("Unable to construct viewmodel")
//        }
//    }
}



