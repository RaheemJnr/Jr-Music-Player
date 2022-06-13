package com.raheemjnr.jr_music.ui.screens.local

import android.Manifest
import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.raheemjnr.jr_music.ui.viewmodels.MainViewModel
import com.raheemjnr.jr_music.utils.Permissions
import com.raheemjnr.jr_music.utils.askPermissions
import com.raheemjnr.jr_music.utils.hasPermissions

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

    val audios = viewModel.audios.observeAsState(initial = null)

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
            viewModel.loadAudios()
//            if (hasPermissions(
//                    context,
//                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                )
//            ) {
//                askPermissions(
//                    context, requestCode = 100, Manifest.permission.READ_EXTERNAL_STORAGE,
//                )
//
//            } else {
//                viewModel.loadAudios()
//            }
        }
        ) {
            Text(text = "show audios")
        }
        //
        val multiplePermissionsState = rememberMultiplePermissionsState(
            listOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )
        )
        Permissions(
            multiplePermissionsState = multiplePermissionsState,
            context = context,
            rationaleText = "Hi",
            modifier = Modifier,
        ) {
            Text(text = "${audios.value}")
        }

//            LazyColumn() {
//                items(items = audios,
//
//                ) {
//
//                }
//                items(items = audios,
//                    key = {
//                    }
//                ) { item ->
//                    item?.let {
//                        Column {
//                            CryptoListItems(
//                                items = item
//                            ) {
//                                //on item click
//                                navController.navigate(
//                                    route =
//                                    "${MainScreen.DetailScreen.route}/${item.id}/${item.symbol}"
//                                )
//                            }
//                        }
//                    }
//                }
////                audios.value?.let { item ->
////                    items(i
////                    ) {
////                        Card(
////                            backgroundColor = Color.Red,
////                            modifier = Modifier
////                                .padding(4.dp)
////                                .fillMaxWidth(),
////                            elevation = 8.dp,
////                        ) {
////                            Text(
////                                text = item.,
////                                fontWeight = FontWeight.Bold,
////                                fontSize = 30.sp,
////                                color = Color(0xFFFFFFFF),
////                                textAlign = TextAlign.Center,
////                                modifier = Modifier.padding(16.dp)
////                            )
////                        }
////                    }
////                }
//
//            }


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