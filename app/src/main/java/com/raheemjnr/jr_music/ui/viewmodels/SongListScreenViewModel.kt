package com.raheemjnr.jr_music.ui.viewmodels

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.raheemjnr.jr_music.R
import com.raheemjnr.jr_music.data.model.Songs
import com.raheemjnr.jr_music.media.EMPTY_PLAYBACK_STATE
import com.raheemjnr.jr_music.media.MusicServiceConnection
import com.raheemjnr.jr_music.media.NOTHING_PLAYING
import com.raheemjnr.jr_music.media.extentions.id
import com.raheemjnr.jr_music.media.extentions.isPlaying
import com.raheemjnr.jr_music.utils.Constants.NO_RES
import com.raheemjnr.jr_music.utils.Constants.mediaId
import javax.inject.Inject

class SongListScreenViewModel @Inject constructor(
    musicServiceConnection: MusicServiceConnection
) : ViewModel() {
    /**
     * Use a backing property so consumers of mediaItems only get a [LiveData] instance so
     * they don't inadvertently modify it.
     */
    private val _audio = MutableLiveData<List<Songs>>()
    val audios: LiveData<List<Songs>> get() = _audio


    private val subscriptionCallback = object : MediaBrowserCompat.SubscriptionCallback() {
        override fun onChildrenLoaded(
            parentId: String,
            children: List<MediaBrowserCompat.MediaItem>
        ) {
        }
    }


    /**
    //     * When the session's [PlaybackStateCompat] changes, the [mediaItems] need to be updated
    //     * so the correct [MediaItemData.playbackRes] is displayed on the active item.
    //     * (i.e.: play/pause button or blank)
    //     */


    private val playbackStateObserver = Observer<PlaybackStateCompat> {
        val playbackState = it ?: EMPTY_PLAYBACK_STATE
        val metadata = musicServiceConnection.nowPlaying.value ?: NOTHING_PLAYING
        if (metadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID) != null) {
            _audio.postValue(updateState(playbackState, metadata))
        }
    }

    /**
     * When the session's [MediaMetadataCompat] changes, the [mediaItems] need to be updated
     * as it means the currently active item has changed. As a result, the new, and potentially
     * old item (if there was one), both need to have their [MediaItemData.playbackRes]
     * changed. (i.e.: play/pause button or blank)
     */
    private val mediaMetadataObserver = Observer<MediaMetadataCompat> {
        val playbackState = musicServiceConnection.playbackState.value ?: EMPTY_PLAYBACK_STATE
        val metadata = it ?: NOTHING_PLAYING
        if (metadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID) != null) {
            _audio.postValue(updateState(playbackState, metadata))
        }
    }

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

    private val musicServiceConnection = musicServiceConnection.also {
        it.subscribe(mediaId, subscriptionCallback)

        it.playbackState.observeForever(playbackStateObserver)
        it.nowPlaying.observeForever(mediaMetadataObserver)
    }

    private fun updateState(
        playbackState: PlaybackStateCompat,
        mediaMetadata: MediaMetadataCompat
    ): List<Songs> {

        val newResId = when (playbackState.isPlaying) {
            true -> R.drawable.morevert
            else -> R.drawable.play_button
        }

        return _audio.value?.map {
            val useResId = if (it.id == mediaMetadata.id) newResId else NO_RES
            it.copy(id = useResId.toString())
        } ?: emptyList()
    }


    override fun onCleared() {
        // Remove the permanent observers from the MusicServiceConnection.
        musicServiceConnection.playbackState.removeObserver(playbackStateObserver)
        musicServiceConnection.nowPlaying.removeObserver(mediaMetadataObserver)

        // And then, finally, unsubscribe the media ID that was being watched.
        musicServiceConnection.unsubscribe(mediaId, subscriptionCallback)
    }

}