package com.raheemjnr.jr_music.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.material.TabPosition
import androidx.compose.ui.Modifier
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import java.util.*


const val TAG = "MainActivityVM"


/**
function to show toast message
 */
fun showToast(context: Context, message: String, length: Int) {
    Toast.makeText(context, message, length).show()
}

//@OptIn(ExperimentalPagerApi::class)
//fun Modifier.pagerTabIndicatorOffset(
//    pagerState: PagerState,
//    tabPositions: List<TabPosition>,
//    pageIndexMapping: (Int) -> Int = { it }
//)

data class TabItems(
    val value: String,
)

//greetings
fun getGreeting(): String {
    val c: Calendar = Calendar.getInstance()

    return when (c.get(Calendar.HOUR_OF_DAY)) {
        in 6..11 -> {
            "Good Morning"
        }
        in 12..15 -> {
            "Good Afternoon"
        }
        in 16..24 -> {
            "Good Evening"
        }
        else -> {
            "Good Night"
        }
    }
}