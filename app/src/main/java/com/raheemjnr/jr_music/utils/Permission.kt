package com.raheemjnr.jr_music.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.raheemjnr.jr_music.ui.viewmodels.MainViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Permissions(
    multiplePermissionsState: MultiplePermissionsState,
    context: Context,
    rationaleText: String,
    modifier: Modifier,
    viewModel: MainViewModel
//    whatToOpen: @Composable () -> Unit = {}
) {
    // Track if the user doesn't want to see the rationale any more.
    var doNotShowRationale by rememberSaveable { mutableStateOf(false) }

    when {
        // If all permissions are granted, then show screen with the feature enabled
        multiplePermissionsState.allPermissionsGranted -> {
            //content to display when permission is granted
            val audio = viewModel.audios.observeAsState(initial = null)
            Text(text = "$audio")

        }
        /*
        If the user denied any permission but a rationale should be shown, or the user sees
        the permissions for the first time, explain why the feature is needed by the app and
        allow the user decide if they don't want to see the rationale any more.
        */
        multiplePermissionsState.shouldShowRationale ||
                !multiplePermissionsState.permissionRequested -> {
            if (doNotShowRationale) {
                DeniedText(rationaleText = rationaleText, context = context)
            } else {
                PermissionDialog(
                    text = "Camera and Location are important. " +
                            "Please grant all of them for the app to function properly.",
                    dismiss = { doNotShowRationale = true },
                    onRequestPermission = { multiplePermissionsState.launchMultiplePermissionRequest() }
                )
            }
        }
        /*
        If the criteria above hasn't been met, the user denied some permission. Let's present
        the user with a FAQ in case they want to know more and send them to the Settings screen
        to enable them the future there if they want to.
        */
        else -> {
            DeniedText(rationaleText = rationaleText, context = context)
        }
    }
}

@Composable
fun DeniedText(
    rationaleText: String,
    context: Context
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Camera and Location permissions are denied.$rationaleText"
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                context.startActivity(
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                )
            }
        ) {
            Text("Open Settings")
        }
    }
}

//
@Composable
fun PermissionDialog(
    text: String,
    onRequestPermission: () -> Unit,
    dismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { dismiss() },
        title = {
            Text(
                text = "Permission Request",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Text(
                text,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        },
        confirmButton = {
            Button(onClick = onRequestPermission) {
                Text("Grant")
            }
        }
    )
}
