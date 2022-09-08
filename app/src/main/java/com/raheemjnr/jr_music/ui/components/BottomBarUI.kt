package com.raheemjnr.jr_music.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.raheemjnr.jr_music.ui.viewmodels.MainViewModel


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BottomBarUI(
    mainViewModel: MainViewModel,
    navController: NavHostController
) {
    Column {
        //bottom music controller
        BottomTrackController(
            trackName = "HH",
            seekState = 0.3F,
            imageUrl = "",
            nowPlayingClicked = {
                mainViewModel.isCollapsed.postValue(false)
            },
            artistName = "Jnr",
            onChangePlayerClicked = { /*TODO*/ },
            hasLiked = true,
            onLikeClicked = {}
        )
        //bottom nav
        BottomAppBar(
            modifier = Modifier
                .height(60.dp),
            backgroundColor = MaterialTheme.colors.primary,
            elevation = 22.dp,
        ) {
            BottomNav(
                navController = navController,
            )
        }
    }
}