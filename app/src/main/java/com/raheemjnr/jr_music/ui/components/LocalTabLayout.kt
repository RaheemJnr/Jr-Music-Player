package com.raheemjnr.jr_music.ui.components

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.raheemjnr.jr_music.data.model.Songs
import com.raheemjnr.jr_music.ui.viewmodels.MainViewModel
import com.raheemjnr.jr_music.utils.ComposablePermission
import com.raheemjnr.jr_music.utils.TabItems
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun LocalTabLayout(
    pagerState: PagerState,
    scope: CoroutineScope,
    viewModel: MainViewModel,
    audios: State<List<Songs>?>
) {
    val tabsTitles =
        remember { listOf(TabItems("Songs"), TabItems("Albums"), TabItems("Artists")) }

    TabRow(
// Our selected tab is our current page
        selectedTabIndex = pagerState.currentPage,
// Override the indicator, using the provided pagerTabIndicatorOffset modifier
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier
                    .pagerTabIndicatorOffset(pagerState, tabPositions)
                    .wrapContentSize()
                    .background(color = Color.Green, shape = RoundedCornerShape(12.dp)),
                height = 5.dp,
                color = Color.Green
            )
        }
    ) {
        // Add tabs for all of our pages
        tabsTitles.forEachIndexed { index, title ->
            Tab(
                text = { Text(text = title.value) },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(page = index)
                    }
                },
            )
        }
    }

    HorizontalPager(
        count = tabsTitles.size,
        state = pagerState,
    ) { page ->
        when (page) {
            0 -> {
                //list button
                Button(onClick = {
                    viewModel.loadAudios()
                }
                ) {
                    Text(text = "show audios")
                }

                //audio list
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
            1 -> {
                Text(
                    text = "page $page",
                    modifier = Modifier.fillMaxSize()
                )
            }
            2 -> {
                Text(
                    text = "page $page",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
