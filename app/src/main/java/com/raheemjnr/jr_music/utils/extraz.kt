package com.raheemjnr.jr_music.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

//check if permission is already granted
fun hasPermissions(context: Context, vararg permissions: String) =
    permissions.all { permission ->
        ActivityCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

//else ask permission
fun askPermissions(context: Context, requestCode: Int, vararg permissions: String) {
    ActivityCompat.requestPermissions(
        context as Activity,
        permissions,
        requestCode
    )
}

const val TAG = "MainActivityVM"