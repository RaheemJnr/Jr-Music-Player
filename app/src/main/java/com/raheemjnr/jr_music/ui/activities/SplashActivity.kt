package com.raheemjnr.jr_music.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.raheemjnr.jr_music.ui.theme.JrMusicPlayerTheme
import com.raheemjnr.jr_music.utils.Navigator

class SplashActivity : ComponentActivity() {

    companion object {
        private const val DELAY_MILLIS = 1000L
        private const val LOCATION_PERMISSION_REQUEST_ID = 1
    }

    lateinit var navigator: Navigator

    private val handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeFullScreen()
        setContent {
            JrMusicPlayerTheme {
                goToMain()
            }
        }


    }

    private fun makeFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        actionBar?.hide()
    }


    private fun goToMain() =
        handler.post {
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

