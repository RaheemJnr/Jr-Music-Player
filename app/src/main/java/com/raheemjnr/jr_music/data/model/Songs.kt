package com.raheemjnr.jr_music.data.model

import android.net.Uri

/**
 * Simple data class to hold information about audio included in the device's MediaStore.
 */
data class Songs(
    val id: Long,
    val title: String,
    val contentUri: Uri
)