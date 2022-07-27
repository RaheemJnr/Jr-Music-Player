package com.raheemjnr.jr_music.data.model

import android.net.Uri

/**
 * Simple data class to hold information about audio included in the device's MediaStore.
 */
data class Songs(
    val id: Long,
    val title: String,
    val album: String,
    val artist: String,
    val image: String,
    val duration: Long = -1,
    val contentUri: Uri
)

//interface Item {
//    val user : String
//    val title : String
//    val playlistTitle : String
//    val id : String
//    var isFavorite: Boolean
//    val songIconList : SongIconList
//}
//@Suppress("unused")
//class JsonMusic {
//    var id: String = ""
//    var title: String = ""
//    var album: String = ""
//    var artist: String = ""
//    var genre: String = ""
//    var source: String = ""
//    var image: String = ""
//    var trackNumber: Long = 0
//    var totalTrackCount: Long = 0
//    var duration: Long = -1
//    var site: String = ""
//}