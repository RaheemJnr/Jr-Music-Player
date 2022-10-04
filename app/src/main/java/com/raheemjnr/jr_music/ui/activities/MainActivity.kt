package com.raheemjnr.jr_music.ui.activities


import android.media.AudioManager
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.raheemjnr.jr_music.di.InjectorUtils
import com.raheemjnr.jr_music.ui.screens.MainScreen
import com.raheemjnr.jr_music.ui.theme.JrMusicPlayerTheme
import com.raheemjnr.jr_music.ui.viewmodels.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JrMusicPlayerTheme {


                window.statusBarColor = MaterialTheme.colors.surface.toArgb()
                window.navigationBarColor = MaterialTheme.colors.surface.toArgb()

                @Suppress("DEPRECATION")
                if (MaterialTheme.colors.surface.luminance() > 0.5f) {
                    window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
                @Suppress("DEPRECATION")
                if (MaterialTheme.colors.surface.luminance() > 0.5f) {
                    window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or
                            View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                }

                // A surface container using the 'background' color from the theme
                //
                val mainViewModel: MainViewModel = viewModel(
                    factory = InjectorUtils.provideMainViewModel(context = this)
                )

                // Since this is a music player, the volume controls should adjust the music volume while
                // in the app.
                volumeControlStream = AudioManager.STREAM_MUSIC
                //
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen(mainViewModel = mainViewModel)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JrMusicPlayerTheme {
    }
}

