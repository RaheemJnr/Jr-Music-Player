package com.raheemjnr.jr_music.navigation


import MainScreen
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.raheemjnr.jr_music.ui.screens.local.LocalMusicScreen
import com.raheemjnr.jr_music.ui.screens.online.OnlineMusicScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun MainScreenNavigation(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>
) {

    /*** main viewModel   */
//    val mainViewModel: MainViewModel = viewModel(
//        factory = MainViewModelFactory(
//        )
//    )
    NavHost(navController, startDestination = MainScreen.Local.route!!) {
        //local
        composable(MainScreen.Local.route) {
            LocalMusicScreen()
        }
        //online
        composable(MainScreen.Online.route!!) {
           OnlineMusicScreen()
        }


    }

}

