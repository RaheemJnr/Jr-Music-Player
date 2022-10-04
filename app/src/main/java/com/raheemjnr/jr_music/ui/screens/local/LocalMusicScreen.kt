package com.raheemjnr.jr_music.ui.screens.local

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.raheemjnr.jr_music.di.InjectorUtils
import com.raheemjnr.jr_music.ui.components.localScreen.CustomTopBar
import com.raheemjnr.jr_music.ui.components.localScreen.LocalTabLayout
import com.raheemjnr.jr_music.ui.components.localScreen.MainUiCard
import com.raheemjnr.jr_music.ui.viewmodels.MainViewModel


@OptIn(ExperimentalPagerApi::class)
@Composable
fun LocalMusicScreen(
) {
    //viewModel
    val viewModel: MainViewModel = viewModel()
    //context
    val context = LocalContext.current as Activity
    //
    val audios = viewModel.audios.observeAsState()
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    viewModel.loadAudios()


    //root composable
    Column(
        Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = { CustomTopBar(context = context) }

        ) { contentPadding ->
            Column(
                modifier = Modifier.padding(contentPadding)
            ) {
                MainUiCard()
                Spacer(modifier = Modifier.height(4.dp))

                LocalTabLayout(
                    pagerState = pagerState,
                    scope = scope,
                    viewModel = viewModel,
                    audios = audios,
                    context = context,
                    //InjectorUtils.provideMusicServiceConnection(context)
                )


            }

        }
    }
}

@Preview
@Composable
fun LocalMusicScreenPreview() {
    // LocalMusicScreen()
}




