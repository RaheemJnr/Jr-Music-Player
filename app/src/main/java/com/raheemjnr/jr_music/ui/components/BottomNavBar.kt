package com.raheemjnr.jr_music.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

@ExperimentalAnimationApi
@Composable
fun BottomNav(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination


    BottomNavigation(
        modifier = Modifier
            .padding(40.dp, 0.dp, 30.dp, 0.dp)
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

val items = listOf(MainScreen.Local, MainScreen.Gap, MainScreen.Online)