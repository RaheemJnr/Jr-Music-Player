package com.raheemjnr.jr_music.ui.components.nowPlaying

import android.app.Activity
import android.view.View
import android.view.Window
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import com.raheemjnr.jr_music.R
import com.raheemjnr.jr_music.ui.theme.black
import com.raheemjnr.jr_music.ui.theme.spotifyGray
import com.raheemjnr.jr_music.ui.viewmodels.MainViewModel

@ExperimentalMaterialApi
@Composable
fun NowPlaying(
    mainViewModel: MainViewModel
) {

    val scrollState = rememberScrollState()
    val context = (LocalContext.current) as Activity

    context.window.statusBarColor = context.getColor(R.color.white)
//    @Composable
//    fun SystemUi(windows: Window) =
//        MaterialTheme {
//            windows.statusBarColor = MaterialTheme.colors.surface.toArgb()
//            windows.navigationBarColor = MaterialTheme.colors.surface.toArgb()
//
//            @Suppress("DEPRECATION")
//            if (MaterialTheme.colors.surface.luminance() > 0.5f) {
//                windows.decorView.systemUiVisibility = windows.decorView.systemUiVisibility or
//                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//            }
//
//            @Suppress("DEPRECATION")
//            if (MaterialTheme.colors.surface.luminance() > 0.5f) {
//                windows.decorView.systemUiVisibility = windows.decorView.systemUiVisibility or
//                        View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
//            }
//        }


//    val albumArtLink by musicPlayerViewModel.imageUrl.observeAsState()
//    val songName: String? by musicPlayerViewModel.trackName.observeAsState(initial = "")
//    val singerName: String? by musicPlayerViewModel.singerName.observeAsState(initial = "")
//    val seekPosition: Float by musicPlayerViewModel.seekState.observeAsState(initial = 0.0f)
//    val albumName by musicPlayerViewModel.albumName.observeAsState()
//    val likesThisSong by musicPlayerViewModel.likesThisSong.observeAsState(initial = false)
    var temporarySeekPosition by remember { mutableStateOf(0f) }
    val colors = remember {
        mutableStateOf(arrayListOf<Color>(black, spotifyGray, black))
    }

//    val isPaused: Boolean by musicPlayerViewModel.isPlaying.observeAsState(initial = false)
//
//    val repeatMode by musicPlayerViewModel.repeatMode.observeAsState()
//
//
//    val paletteExtractor = PaletteExtractor()
//    albumArtLink?.let {
//        val shade = paletteExtractor.getColorFromSwatch(it)
//        shade.observeForever { shadeColor ->
//            shadeColor?.let { col ->
//                colors.value = arrayListOf(col, spotifyDarkBlack)
//            }
//
//        }
//    }

    Column(
        Modifier
            .fillMaxSize()
            .scrollable(scrollState, Orientation.Vertical)
            .background(
                Brush.linearGradient(colors = colors.value)
            )
            .pointerInput(Unit) {
                detectVerticalDragGestures { change, _ ->
                    if (change.position.y - change.previousPosition.y > 20f) {
                        mainViewModel.isCollapsed.postValue(true)
                    }
                }
            }
    ) {

        NowPlayingTopBar(
            albumName = "Raheem Jnr"
        ) {
            mainViewModel.isCollapsed.postValue(true)
        }
//        AlbumArt(albumArtLink)
//        CurrentSong(
//            songName = songName,
//            singerName = singerName,
//        )
//        SeekBar(seekPosition, onValueChanged = {
//            musicPlayerViewModel.updateSeekState(temporarySeekPosition)
//            musicPlayerViewModel.seekTo(temporarySeekPosition)
//        }) {
//            temporarySeekPosition = it
////
////
//        }
//        Spacer(modifier = Modifier.height(8.dp))
//        Controller(
//            repeatMode,
//            isPaused = isPaused,
//            onLiked = {
//                musicPlayerViewModel.spotifyRemote.value?.playerApi?.toggleRepeat()
//
//            },
//            onNext = {
//                musicPlayerViewModel.spotifyRemote.value?.playerApi?.skipNext()
//            },
//            onPrevious = {
//                musicPlayerViewModel.spotifyRemote.value?.playerApi?.skipPrevious()
//
//            }, likesThisSong = likesThisSong, onHeartClick = {
//                musicPlayerViewModel.toggleLikeSong()
//            }, onPause = {
//
//                if (!isPaused) {
//                    Log.d("TAG", "NowPlaying: Resuming")
//                    musicPlayerViewModel.spotifyRemote.value?.playerApi?.resume()
//                } else {
//
//                    Log.d("TAG", "NowPlaying: Pausing")
//                    musicPlayerViewModel.spotifyRemote.value?.playerApi?.pause()
//                }
//            }

        //)
    }


}
