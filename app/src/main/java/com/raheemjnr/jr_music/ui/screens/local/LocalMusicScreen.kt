package com.raheemjnr.jr_music.ui.screens.local

import android.Manifest
import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.raheemjnr.jr_music.data.model.Songs
import com.raheemjnr.jr_music.ui.viewmodels.MainViewModel
import com.raheemjnr.jr_music.utils.ComposablePermission

/**
 * Code used with [IntentSender] to request user permission to delete an image with scoped storage.
 */
private const val DELETE_PERMISSION_REQUEST = 0x1033

@OptIn(ExperimentalPermissionsApi::class, ExperimentalFoundationApi::class)
@Composable
fun LocalMusicScreen() {
    //viewModel
    val viewModel: MainViewModel = viewModel()
    //context
    val context = LocalContext.current as Activity

    val audios = viewModel.audios.observeAsState()

//    val intentSender = viewModel.permissionNeededForDelete.observeAsState()
//
//    // On Android 10+, if the app doesn't have permission to modify
//    // or delete an item, it returns an `IntentSender` that we can
//    // use here to prompt the user to grant permission to delete (or modify)
//    // the image.
//    intentSender.value?.let { intentSender ->
//        startIntentSenderForResult(
//            context,
//            intentSender,
//            DELETE_PERMISSION_REQUEST,
//            null,
//            0,
//            0,
//            0,
//            null
//        )
//    }


    Column(
        Modifier.fillMaxSize()
    ) {
        Button(onClick = {
//            if (!hasPermissions(
//                    context,
//                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                )
//            ) {
//                askPermissions(
//                    context, requestCode = 100, Manifest.permission.READ_EXTERNAL_STORAGE,
//                )
//
//            }
            viewModel.loadAudios()

        }
        ) {
            Text(text = "show audios")
        }
        Box(modifier = Modifier)
        {
            ComposablePermission(permission = Manifest.permission.READ_EXTERNAL_STORAGE,
                onDenied = {}) {
                LazyColumn() {
                    audios.value?.let { item ->
                        items(
                            items = item,
                            key = {
                                it.id
                            }
                        ) { item: Songs ->
                            Text(text = "$item")

                        }
                    }

                }
            }
        }

    }
}


//private fun deleteImage(image: MediaAudio) {
//    MaterialAlertDialogBuilder(this)
//        .setTitle(R.string.delete_dialog_title)
//        .setMessage(getString(R.string.delete_dialog_message, image.displayName))
//        .setPositiveButton(R.string.delete_dialog_positive) { _: DialogInterface, _: Int ->
//            viewModel.deleteImage(image)
//        }
//        .setNegativeButton(R.string.delete_dialog_negative) { dialog: DialogInterface, _: Int ->
//            dialog.dismiss()
//        }
//        .show()
//}

