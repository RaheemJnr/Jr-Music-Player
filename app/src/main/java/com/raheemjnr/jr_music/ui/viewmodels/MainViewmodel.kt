package com.raheemjnr.jr_music.ui.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.app.RecoverableSecurityException
import android.content.IntentSender
import android.database.ContentObserver
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raheemjnr.jr_music.data.model.Songs
import com.raheemjnr.jr_music.utils.loadMusic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _audio = MutableLiveData<List<Songs>>()
    val audios: LiveData<List<Songs>> get() = _audio

    private var contentObserver: ContentObserver? = null

    private var pendingDeleteAudio: Songs? = null
    private val _permissionNeededForDelete = MutableLiveData<IntentSender?>()
    val permissionNeededForDelete: LiveData<IntentSender?> = _permissionNeededForDelete

    /**
     * Performs a one shot load of audios from [MediaStore.audio.Media.EXTERNAL_CONTENT_URI] into
     * the [_audio] [LiveData] above.
     */
    fun loadAudios() {
        loadMusic(getApplication(), _audio)
    }

//    private fun load() {
//        viewModelScope.launch {
//            val audioList = queryAudios(getApplication())
//            _audio.postValue(audioList)
//
//            if (contentObserver == null) {
//                contentObserver = getApplication<Application>().contentResolver.registerObserver(
//                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
//                ) {
//                    loadAudios()
//                }
//            }
//        }
//    }

    fun deleteAudio(audio: Songs) {
        viewModelScope.launch {
            performDeleteImage(audio)
        }
    }

    fun deletePendingAudio() {
        pendingDeleteAudio?.let { image ->
            pendingDeleteAudio = null
            deleteAudio(image)
        }
    }


    private suspend fun performDeleteImage(audio: Songs) {
        withContext(Dispatchers.IO) {
            try {
                /**
                 * In [Build.VERSION_CODES.Q] and above, it isn't possible to modify
                 * or delete items in MediaStore directly, and explicit permission
                 * must usually be obtained to do this.
                 *
                 * The way it works is the OS will throw a [RecoverableSecurityException],
                 * which we can catch here. Inside there's an [IntentSender] which the
                 * activity can use to prompt the user to grant permission to the item
                 * so it can be either updated or deleted.
                 */
                getApplication<Application>().contentResolver.delete(
                    audio.contentUri,
                    "${MediaStore.Images.Media._ID} = ?",
                    arrayOf(audio.id.toString())
                )
            } catch (securityException: SecurityException) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val recoverableSecurityException =
                        securityException as? RecoverableSecurityException
                            ?: throw securityException

                    // Signal to the Activity that it needs to request permission and
                    // try the delete again if it succeeds.
                    pendingDeleteAudio = audio
                    _permissionNeededForDelete.postValue(
                        recoverableSecurityException.userAction.actionIntent.intentSender
                    )
                } else {
                    throw securityException
                }
            }
        }
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
     * Since we register a [ContentObserver], we want to unregister this when the `ViewModel`
     * is being released.
     */
    override fun onCleared() {
        contentObserver?.let {
            getApplication<Application>().contentResolver.unregisterContentObserver(it)
        }
    }
}



