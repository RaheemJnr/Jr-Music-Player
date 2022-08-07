package com.raheemjnr.jr_music.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable


const val TAG = "MainActivityVM"


/**
function to show toast message
 */
fun showToast(context: Context, message: String, length: Int) {
    Toast.makeText(context, message, length).show()
}

