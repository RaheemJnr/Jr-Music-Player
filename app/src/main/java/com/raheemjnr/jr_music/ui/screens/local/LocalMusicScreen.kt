package com.raheemjnr.jr_music.ui.screens.local

import android.Manifest
import android.app.Activity
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
import com.raheemjnr.jr_music.data.model.Songs
import com.raheemjnr.jr_music.ui.viewmodels.MainViewModel
import com.raheemjnr.jr_music.utils.ComposablePermission

/**
 * Code used with [IntentSender] to request user permission to delete an image with scoped storage.
 */
private const val DELETE_PERMISSION_REQUEST = 0x1033

@Composable
fun LocalMusicScreen() {
    //viewModel
    val viewModel: MainViewModel = viewModel()
    //context
    LocalContext.current as Activity

    val audios = viewModel.audios.observeAsState()




    Column(
        Modifier.fillMaxSize()
    ) {
        Button(onClick = {
            viewModel.loadAudios()
        }
        ) {
            Text(text = "show audios")
        }
        Box(modifier = Modifier)
        {
            ComposablePermission(permission = Manifest.permission.READ_EXTERNAL_STORAGE,
                onDenied = {
                    Text(text = "Permission Denied")
                }
            ) {
                LazyColumn() {
                    audios.value?.let { itemm ->
                        items(
                            items = itemm,
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




