package com.raheemjnr.jr_music.ui.screens.local

import android.Manifest
import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.raheemjnr.jr_music.data.model.Songs
import com.raheemjnr.jr_music.ui.components.CustomTopBar
import com.raheemjnr.jr_music.ui.components.MainUiCard
import com.raheemjnr.jr_music.ui.viewmodels.MainViewModel
import com.raheemjnr.jr_music.utils.ComposablePermission


@OptIn(ExperimentalPagerApi::class)
@Composable
fun LocalMusicScreen() {
    //viewModel
    val viewModel: MainViewModel = viewModel()
    //context
    val context = LocalContext.current as Activity

    val audios = viewModel.audios.observeAsState()

    data class TabItems(
        val value: String,
    )

    //root composable
    Column(
        Modifier.fillMaxSize()
    ) {
        val tabsNameList =
            remember { listOf(TabItems("Songs"), TabItems("Songs"), TabItems("Songs")) }
        Scaffold(
            topBar = { CustomTopBar(context = context) }

        ) { contentPadding ->
            Column(
                modifier = Modifier.padding(contentPadding)
            ) {
                MainUiCard()
                Spacer(modifier = Modifier.height(50.dp))

                val pagerState = rememberPagerState()

                TabRow(
                    // Our selected tab is our current page
                    selectedTabIndex = pagerState.currentPage,
                    // Override the indicator, using the provided pagerTabIndicatorOffset modifier
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                        )
                    }
                ) {
                    // Add tabs for all of our pages
                    tabsNameList.forEachIndexed { index, title ->
                        Tab(
                            text = { Text(text = title.value) },
                            selected = pagerState.currentPage == index,
                            onClick = { /* TODO */ },
                        )
                    }
                }

                HorizontalPager(
                    count = tabsNameList.size,
                    state = pagerState,
                ) { page ->
                    Text(text = "$page")
                }
                Button(onClick = {
                    viewModel.loadAudios()
                }
                ) {
                    Text(text = "show audios")
                }
                Box(modifier = Modifier)
                {
                    ComposablePermission(permission = Manifest.permission.READ_EXTERNAL_STORAGE,
                        onDenied = {
                            Text(text = "Permission Denied")
                        }
                    ) {
                        LazyColumn() {
                            audios.value?.let { itemm ->
                                items(
                                    items = itemm,
                                    key = {
                                        it.id
                                    }
                                ) { item: Songs ->
                                    Text(text = "$item")
                                }
                            }

                        }
                    }
                }
            }

        }


    }
}

@Preview
@Composable
fun LocalMusicScreenPreview() {
    LocalMusicScreen()
}




