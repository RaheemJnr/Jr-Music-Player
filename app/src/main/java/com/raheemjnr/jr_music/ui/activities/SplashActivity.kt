package com.raheemjnr.jr_music.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.raheemjnr.jr_music.ui.theme.JrMusicPlayerTheme
import com.raheemjnr.jr_music.utils.ComposablePermission
import com.raheemjnr.jr_music.utils.Permissions

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {


    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeFullScreen()
        setContent {
            val multiplePermissionsState = rememberMultiplePermissionsState(
                listOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
            JrMusicPlayerTheme {
                //
                Permissions(
                    multiplePermissionsState = multiplePermissionsState,
                    context = this,
                    rationaleText = "But for this app to serve you properly we need this permission. Please, grant us access on the Settings screen.",
                ) {
                    goToMain()
                }
            }
        }
    }

    /* make full screen
    *  */
    private fun makeFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            FLAG_FULLSCREEN,
            FLAG_FULLSCREEN
        )
        actionBar?.hide()
    }

    /* navigate to main
    * ¶ */
    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

//    private fun requestLocationPermission() {
//        if (ActivityCompat.shouldShowRequestPermissionRationale(
//                this,
//                Manifest.permission.READ_EXTERNAL_STORAGE
//            )
//        ) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                LOCATION_PERMISSION_REQUEST_ID
//            )
//            // Show an explanation to the user *asynchronously* -- don't block
//            // this thread waiting for the user's response! After the user
//            // sees the explanation, try again to request the permission.
//        } else {
//            // No explanation needed, we can request the permission.
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                LOCATION_PERMISSION_REQUEST_ID
//            )
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        when (requestCode) {
//            LOCATION_PERMISSION_REQUEST_ID -> {
//                // If request is cancelled, the result arrays are empty.
//                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                    // Permission granted! We go on!
//                    goToMain()
//                } else {
//                    // Request denied, we request again
//                    requestLocationPermission()
//                }
//            }
//        }
//    }
}

