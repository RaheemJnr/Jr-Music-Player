@file:Suppress("DEPRECATION")

package com.raheemjnr.jr_music.utils

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.IntentSender
import android.database.ContentObserver
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.provider.BaseColumns
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.raheemjnr.jr_music.data.model.Songs
import kotlinx.coroutines.*


private var pendingDeleteAudio: Songs? = null
private val _permissionNeededForDelete = MutableLiveData<IntentSender?>()
val permissionNeededForDelete: LiveData<IntentSender?> = _permissionNeededForDelete

//
private val serviceJob = SupervisorJob()
private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)
private var contentObserver: ContentObserver? = null

///load music to livedata observerl

// query content provider for all mp3 files and related data
private suspend fun queryAudios(context: Context): List<Songs> {
    val audios = mutableListOf<Songs>()

    /**
     * Working with [ContentResolver]s can be slow, so we'll do this off the main
     * thread inside a coroutine.
     */
    withContext(Dispatchers.IO) {

        val collection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Audio.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL
                )
            } else {
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }

        /**
         * A key concept when working with Android [ContentProvider]s is something called
         * "projections". A projection is the list of columns to request from the provider,
         * and can be thought of (quite accurately) as the "SELECT ..." clause of a SQL
         * statement.
         *
         * It's not _required_ to provide a projection. In this case, one could pass `null`
         * in place of `projection` in the call to [ContentResolver.query], but requesting
         * more data than is required has a performance impact.
         *
         * For this sample, we only use a few columns of data, and so we'll request just a
         * subset of columns.
         */
        /**
         * A key concept when working with Android [ContentProvider]s is something called
         * "projections". A projection is the list of columns to request from the provider,
         * and can be thought of (quite accurately) as the "SELECT ..." clause of a SQL
         * statement.
         *
         * It's not _required_ to provide a projection. In this case, one could pass `null`
         * in place of `projection` in the call to [ContentResolver.query], but requesting
         * more data than is required has a performance impact.
         *
         * For this sample, we only use a few columns of data, and so we'll request just a
         * subset of columns.
         */
        val projection = arrayOf(
            BaseColumns._ID,
            MediaStore.Audio.AudioColumns.TITLE,
            MediaStore.Audio.AudioColumns.DURATION,
            MediaStore.Audio.AudioColumns.ALBUM_ID,
            MediaStore.Audio.AudioColumns.ALBUM,
            MediaStore.Audio.AudioColumns.ARTIST,
            MediaStore.Audio.AudioColumns.TRACK,
        )


        /**
         * The `selection` is the "WHERE ..." clause of a SQL statement. It's also possible
         * to omit this by passing `null` in its place, and then all rows will be returned.
         * In this case we're using a selection based on the date the image was taken.
         *
         * Note that we've included a `?` in our selection. This stands in for a variable
         * which will be provided by the next variable.
         */
        val selectionIsMusic =
            MediaStore.Audio.AudioColumns.IS_MUSIC + "=1" + " AND " + MediaStore.Audio.AudioColumns.TITLE + " != '' >= ?"

        /**
         * The `selectionArgs` is a list of values that will be filled in for each `?`
         * in the `selection`.
         */
        val selectionArgs = arrayOf(
            MimeTypeMap.getSingleton().getMimeTypeFromExtension("mp3")
            //TimeUnit.MILLISECONDS.convert(30, TimeUnit.SECONDS).toString()
        )

        /**
         * Sort order to use. This can also be null, which will use the default sort
         * order. For [MediaStore.Images], the default sort order is ascending by date taken.
         */
        val sortOrder = MediaStore.Audio.Media.DEFAULT_SORT_ORDER

        context.applicationContext.contentResolver.query(
            collection,
            projection,
            selectionIsMusic,
            selectionArgs,
            sortOrder
        )?.use { cursor ->
            /**
             * In order to retrieve the data from the [Cursor] that's returned, we need to
             * find which index matches each column that we're interested in.
             *
             * There are two ways to do this. The first is to use the method
             * [Cursor.getColumnIndex] which returns -1 if the column ID isn't found. This
             * is useful if the code is programmatically choosing which columns to request,
             * but would like to use a single method to parse them into objects.
             *
             * In our case, since we know exactly which columns we'd like, and we know
             * that they must be included (since they're all supported from API 1), we'll
             * use [Cursor.getColumnIndexOrThrow]. This method will throw an
             * [IllegalArgumentException] if the column named isn't found.
             *
             * In either case, while this method isn't slow, we'll want to cache the results
             * to avoid having to look them up for each row.
             */
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val albumColumns = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val artistColumns = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumsIdColumns = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            val durationColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

            Log.i(TAG, "Found ${cursor.count} Audios")
            while (cursor.moveToNext()) {

                // Here we'll use the column index that we found above.
                val id = cursor.getLong(idColumn)
                val title = cursor.getString(titleColumn)
                val duration = cursor.getLong(durationColumn)
                val album = cursor.getString(albumColumns)
                val artist = cursor.getString(artistColumns)
                //get album art
                val image = cursor.getLong(albumsIdColumns)
                val sArtworkUri = Uri.parse("content://media/external/audio/albumart")
                val albumArtUri = ContentUris.withAppendedId(
                    sArtworkUri,
                    image
                )

                /**
                 * This is one of the trickiest parts:
                 *
                 * Since we're accessing audios (using
                 * [MediaStore.Audio.Media.EXTERNAL_CONTENT_URI], we'll use that
                 * as the base URI and append the ID of the image to it.
                 *
                 * This is the exact same way to do it when working with [MediaStore.Video] and
                 * [MediaStore.Images] as well. Whatever `Media.EXTERNAL_CONTENT_URI` you
                 * query to get the items is the base, and the ID is the document to
                 * request there.
                 */
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                val audio = Songs(
                    id = id.toString(),
                    title = title,
                    album = album,
                    artist = artist,
                    image = albumArtUri,
                    duration = duration,
                    contentUri = contentUri
                )
                audios += audio

                //For debugging, we'll output the image objects we create to logcat.
                Log.v(TAG, "Added audio: $audios")
            }
        }
    }

    Log.v(TAG, "Found ${audios.size} audios")
    return audios
}


/*
private suspend fun performDeleteImage(audio: Songs) {
withContext(Dispatchers.IO) {
try {
/ **
* In [Build.VERSION_CODES.Q] and above, it isn't possible to modify
* or delete items in MediaStore directly, and explicit permission
* must usually be obtained to do this.
*
* The way it works is the OS will throw a [RecoverableSecurityException],
* which we can catch here. Inside there's an [IntentSender] which the
* activity can use to prompt the user to grant permission to the item
* so it can be either updated or deleted.
* /
context.contentResolver.delete(
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

fun deleteAudio(audio: Songs) {
serviceScope.launch {
performDeleteImage(audio)
}
}

fun deletePendingAudio() {
pendingDeleteAudio?.let { image ->
pendingDeleteAudio = null
deleteAudio(image)
}
}
*/


fun loadMusic(context: Context, audio: MutableLiveData<List<Songs>>): List<Songs>? {
    serviceScope.launch {
        val audioList = queryAudios(context.applicationContext)
        audio.postValue(audioList)

        if (contentObserver == null) {
            contentObserver = context.applicationContext.contentResolver.registerObserver(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            ) {
                loadMusic(context, audio)
            }
        }
    }
    return audio.value
}


/**
 * Convenience extension method to register a [ContentObserver] given a lambda.
 */
fun ContentResolver.registerObserver(
    uri: Uri,
    observer: (selfChange: Boolean) -> Unit
): ContentObserver {
    val contentObserver = object : ContentObserver(Handler()) {
        override fun onChange(selfChange: Boolean) {
            observer(selfChange)
        }
    }
    registerContentObserver(uri, true, contentObserver)
    return contentObserver
}

/*
val intentSender = viewModel.permissionNeededForDelete.observeAsState()

// On Android 10+, if the app doesn't have permission to modify
// or delete an item, it returns an `IntentSender` that we can
// use here to prompt the user to grant permission to delete (or modify)
// the image.
intentSender.value?.let { intentSender ->
startIntentSenderForResult(
context,
intentSender,
DELETE_PERMISSION_REQUEST,
null,
0,
0,
0,
null
)
}
*/
/*
private fun deleteImage(image: MediaAudio) {
MaterialAlertDialogBuilder(this)
.setTitle(R.string.delete_dialog_title)
.setMessage(getString(R.string.delete_dialog_message, image.displayName))
.setPositiveButton(R.string.delete_dialog_positive) { _: DialogInterface, _: Int ->
viewModel.deleteImage(image)
}
.setNegativeButton(R.string.delete_dialog_negative) { dialog: DialogInterface, _: Int ->
dialog.dismiss()
}
.show()
}
*/


/**
 * Code used with [IntentSender] to request user permission to delete an image with scoped storage.
 */
private const val DELETE_PERMISSION_REQUEST = 0x1033