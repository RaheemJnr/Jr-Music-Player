package com.raheemjnr.jr_music.ui.activities


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.raheemjnr.jr_music.navigation.MainScreenNavigation
import com.raheemjnr.jr_music.ui.components.BottomNav
import com.raheemjnr.jr_music.ui.components.BottomTrackController
import com.raheemjnr.jr_music.ui.components.NowPlaying
import com.raheemjnr.jr_music.ui.theme.JrMusicPlayerTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JrMusicPlayerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@OptIn(
    ExperimentalAnimationApi::class, ExperimentalMaterialApi::class,
    ExperimentalComposeUiApi::class, ExperimentalCoroutinesApi::class
)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val isPlayerOpening = remember {
        mutableStateOf(true)
    }
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    BottomSheetScaffold(
        sheetContent = {
            NowPlaying(
                isPlayer = isPlayerOpening.value,
            )
        },
        sheetPeekHeight = 0.dp,
        scaffoldState = bottomSheetScaffoldState,
    ) {
        Scaffold(
            bottomBar = {
                Column {
                    BottomTrackController(
                        trackName = "HH",
                        seekState = 0.5F,
                        imageUrl = "",
                        nowPlayingClicked = { isPlayerOpening.value = true },
                        artistName = "Jnr",
                        isPlaying = true,
                        onChangePlayerClicked = { /*TODO*/ },
                        hasLiked = true
                    ) {

                    }
                    BottomAppBar(
                        modifier = Modifier
                            .height(65.dp)
                            .clip(RoundedCornerShape(0.dp, 0.dp, 0.dp, 0.dp)),
                        cutoutShape = CircleShape,
                        backgroundColor = MaterialTheme.colors.primary,
                        elevation = 22.dp,
                    ) {
                        BottomNav(
                            navController = navController,
                        )
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            isFloatingActionButtonDocked = true,
        ) {
            MainScreenNavigation(navController)
        }
    }

}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JrMusicPlayerTheme {
    }
}

