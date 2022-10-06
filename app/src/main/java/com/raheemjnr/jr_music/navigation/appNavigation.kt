package com.raheemjnr.jr_music.navigation


import MainScreen
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.raheemjnr.jr_music.ui.screens.local.LocalMusicScreen
import com.raheemjnr.jr_music.ui.screens.online.OnlineMusicScreen
import com.raheemjnr.jr_music.ui.viewmodels.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun MainScreenNavigation(
    navController: NavHostController,
) {
    val viewModel = hiltViewModel<MainViewModel>()
    NavHost(navController, startDestination = MainScreen.Local.route!!) {
        //local
        composable(MainScreen.Local.route) {
            LocalMusicScreen(viewModel = viewModel)
        }
        //online
        composable(MainScreen.Online.route!!) {
            OnlineMusicScreen()
        }


    }

}

