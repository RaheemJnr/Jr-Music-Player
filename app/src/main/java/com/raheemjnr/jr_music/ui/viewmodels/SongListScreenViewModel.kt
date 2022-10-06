package com.raheemjnr.jr_music.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class SongListScreenViewModel(
) : ViewModel() {
    /**
     * Use a backing property so consumers of mediaItems only get a [LiveData] instance so
     * they don't inadvertently modify it.
     */
//    private val _mediaItems = MutableLiveData < List<Songs>()
//    val mediaItems: LiveData<List<Songs>> = _mediaItems
//
//    /**
//     * Pass the status of the [MusicServiceConnection.networkFailure] through.
//     */
//    val networkError = Transformations.map(musicServiceConnection.networkFailure) { it }
//
//    private val subscriptionCallback = object : MediaBrowserCompat.SubscriptionCallback() {
//        override fun onChildrenLoaded(
//            parentId: String,
//            children: List<MediaBrowserCompat.MediaItem>
//        ) {
//            val itemsList = children.map { child ->
//                val subtitle = child.description.subtitle ?: ""
//                MediaItemData(
//                    child.mediaId!!,
//                    child.description.title.toString(),
//                    subtitle.toString(),
//                    child.description.iconUri!!,
//                    child.isBrowsable,
//                    getResourceForMediaId(child.mediaId!!)
//                )
//            }
//            _mediaItems.postValue(itemsList)
//        }
//    }

}