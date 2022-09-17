package com.raheemjnr.jr_music.ui.screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.raheemjnr.jr_music.navigation.MainScreenNavigation
import com.raheemjnr.jr_music.ui.components.BottomBarUI
import com.raheemjnr.jr_music.ui.components.nowPlaying.NowPlaying
import com.raheemjnr.jr_music.ui.viewmodels.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalAnimationApi::class, ExperimentalMaterialApi::class,
    ExperimentalComposeUiApi::class, ExperimentalCoroutinesApi::class
)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(mainViewModel: MainViewModel) {
    /*** main viewModel */
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val bottomSheetScaffoldState =
        rememberBottomSheetScaffoldState(bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed))

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
                Surface(elevation = 12.dp) {
                    BottomBarUI(mainViewModel, navController)
                }

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