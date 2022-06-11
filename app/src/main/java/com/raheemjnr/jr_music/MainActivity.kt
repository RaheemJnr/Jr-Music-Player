package com.raheemjnr.jr_music


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.raheemjnr.jr_music.ui.theme.JrMusicPlayerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JrMusicPlayerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
////                   // Need the READ_EXTERNAL_STORAGE permission if accessing video files that your
//                    // app didn't create.
//
//                    // Container for information about each video.
//                    data class Video(
//                        val uri: Uri,
//                        val name: String,
//                        val duration: Int,
//                        val size: Int
//                    )
//
//                    val videoList = mutableListOf<Video>()
//
//                    val collection =
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                            MediaStore.Video.Media.getContentUri(
//                                MediaStore.VOLUME_EXTERNAL
//                            )
//                        } else {
//                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
//                        }
//
//                    val projection = arrayOf(
//                        MediaStore.Video.Media._ID,
//                        MediaStore.Video.Media.DISPLAY_NAME,
//                        MediaStore.Video.Media.DURATION,
//                        MediaStore.Video.Media.SIZE
//                    )
//
//// Show only videos that are at least 5 minutes in duration.
//                    val selection = "${MediaStore.Video.Media.DURATION} >= ?"
//                    val selectionArgs = arrayOf(
//                        TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES).toString()
//                    )
//
//// Display videos in alphabetical order based on their display name.
//                    val sortOrder = "${MediaStore.Video.Media.DISPLAY_NAME} ASC"
//
//                    val query = applicationContext.contentResolver.query(
//                        collection,
//                        projection,
//                        selection,
//                        selectionArgs,
//                        sortOrder
//                    )
//                    query?.use { cursor ->
//                        // Cache column indices.
//                        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
//                        val nameColumn =
//                            cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
//                        val durationColumn =
//                            cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
//                        val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
//
//                        while (cursor.moveToNext()) {
//                            // Get values of columns for a given video.
//                            val id = cursor.getLong(idColumn)
//                            val name = cursor.getString(nameColumn)
//                            val duration = cursor.getInt(durationColumn)
//                            val size = cursor.getInt(sizeColumn)
//
//                            val contentUri: Uri = ContentUris.withAppendedId(
//                                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
//                                id
//                            )
//
//                            // Stores column values and the contentUri in a local object
//                            // that represents the media file.
//                            videoList += Video(contentUri, name, duration, size)
//                        }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JrMusicPlayerTheme {
    }
}