package com.raheemjnr.jr_music.ui.activities


import MainScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.raheemjnr.jr_music.navigation.MainScreenNavigation
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
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .height(65.dp)
                    .clip(RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp)),
                cutoutShape = CircleShape,
                backgroundColor = MaterialTheme.colors.primary,
                elevation = 22.dp,
            ) {
                BottomNav(
                    navController = navController,
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
    ) {
        MainScreenNavigation(navController)
    }
}

@ExperimentalAnimationApi
@Composable
fun BottomNav(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination


    BottomNavigation(
        modifier = Modifier
            .padding(12.dp, 0.dp, 12.dp, 0.dp)
            .height(100.dp),
        backgroundColor = MaterialTheme.colors.primary,
        elevation = 0.dp
    ) {
        items.forEach {
            BottomNavigationItem(
                icon = {
                    it.icon?.let { icon ->
                        Icon(
                            imageVector = icon,
                            contentDescription = "",
                            modifier = Modifier.size(35.dp),
                            tint = Color.LightGray
                        )
                    }
                },
                label = {
                    it.title?.let { labelValue ->
                        Text(
                            text = labelValue,
                            color = Color.LightGray
                        )
                    }
                },
                //
                //
                selected = currentRoute?.hierarchy?.any { it.route == it.route } == true,
                selectedContentColor = MaterialTheme.colors.primary,
                unselectedContentColor = Color.White.copy(alpha = 0.4f),
                onClick = {
                    it.route?.let { destination ->
                        navController.navigate(destination) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // reSelecting the same item
                            launchSingleTop = true
                            // Restore state when reSelecting a previously selected item
                            restoreState = true
                        }
                    }
                }
            )
        }
    }


}

val items = listOf(MainScreen.Local, MainScreen.Online)

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JrMusicPlayerTheme {
    }
}


// Need the READ_EXTERNAL_STORAGE permission if accessing video files that your
//                    // app didn't create.
//
//                    // Container for information about each video.
//                    data class Video(
//                        val uri: Uri,
//                        val name: String,
//                        val duration: Int,
//                        val size: Int
//                    )
//
//                    val videoList = mutableListOf<Video>()
//
//                    val collection =
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                            MediaStore.Video.Media.getContentUri(
//                                MediaStore.VOLUME_EXTERNAL
//                            )
//                        } else {
//                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
//                        }
//
//                    val projection = arrayOf(
//                        MediaStore.Video.Media._ID,
//                        MediaStore.Video.Media.DISPLAY_NAME,
//                        MediaStore.Video.Media.DURATION,
//                        MediaStore.Video.Media.SIZE
//                    )
//
//// Show only videos that are at least 5 minutes in duration.
//                    val selection = "${MediaStore.Video.Media.DURATION} >= ?"
//                    val selectionArgs = arrayOf(
//                        TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES).toString()
//                    )
//
//// Display videos in alphabetical order based on their display name.
//                    val sortOrder = "${MediaStore.Video.Media.DISPLAY_NAME} ASC"
//
//                    val query = applicationContext.contentResolver.query(
//                        collection,
//                        projection,
//                        selection,
//                        selectionArgs,
//                        sortOrder
//                    )
//                    query?.use { cursor ->
//                        // Cache column indices.
//                        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
//                        val nameColumn =
//                            cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
//                        val durationColumn =
//                            cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
//                        val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
//
//                        while (cursor.moveToNext()) {
//                            // Get values of columns for a given video.
//                            val id = cursor.getLong(idColumn)
//                            val name = cursor.getString(nameColumn)
//                            val duration = cursor.getInt(durationColumn)
//                            val size = cursor.getInt(sizeColumn)
//
//                            val contentUri: Uri = ContentUris.withAppendedId(
//                                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
//                                id
//                            )
//
//                            // Stores column values and the contentUri in a local object
//                            // that represents the media file.
//                            videoList += Video(contentUri, name, duration, size)
//                        }