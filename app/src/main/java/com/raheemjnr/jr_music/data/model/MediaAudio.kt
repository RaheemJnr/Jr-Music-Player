package com.raheemjnr.jr_music.data.model

import android.net.Uri

/**
 * Simple data class to hold information about audio included in the device's MediaStore.
 */
data class MediaAudio(
    val id: Long,
    val displayName: String,
    val contentUri: Uri
)
