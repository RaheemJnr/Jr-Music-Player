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
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SongListScreenViewModel @Inject constructor(
    musicServiceConnection: MusicServiceConnection
) : ViewModel() {
    /**
     * Use a backing property so consumers of mediaItems only get a [LiveData] instance so
     * they don't inadvertently modify it.
     */
    private val _audio = MutableLiveData<List<Songs>>()
    val audios: LiveData<List<Songs>> get() = _audio








    override fun onCleared() {

    }

}