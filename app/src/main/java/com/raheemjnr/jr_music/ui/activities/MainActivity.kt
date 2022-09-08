package com.raheemjnr.jr_music.ui.activities


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.raheemjnr.jr_music.navigation.MainScreenNavigation
import com.raheemjnr.jr_music.ui.components.BottomBarUI
import com.raheemjnr.jr_music.ui.components.nowPlaying.NowPlaying
import com.raheemjnr.jr_music.ui.theme.JrMusicPlayerTheme
import com.raheemjnr.jr_music.ui.viewmodels.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

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
    /*** main viewModel   */
    val mainViewModel: MainViewModel = viewModel()
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed))

    mainViewModel.isCollapsed.observeForever {
        it?.let { collapsed ->
            if (collapsed) {
                scope.launch {
                    bottomSheetScaffoldState.bottomSheetState.collapse()
                }
            } else {
                scope.launch {
                    bottomSheetScaffoldState.bottomSheetState.expand()

                }

            }
        }
    }


    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            NowPlaying(mainViewModel = mainViewModel)
        },
    ) {
        Scaffold(
            bottomBar = {
                BottomBarUI(mainViewModel, navController)
            },
            floatingActionButtonPosition = FabPosition.Center,
            isFloatingActionButtonDocked = true,
        ) {

            BackHandler(bottomSheetScaffoldState.bottomSheetState.isExpanded) {
                mainViewModel.isCollapsed.postValue(true)
            }

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

