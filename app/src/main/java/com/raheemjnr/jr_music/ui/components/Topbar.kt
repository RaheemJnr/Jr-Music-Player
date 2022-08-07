package com.raheemjnr.jr_music.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TopBar() {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.LightGray.copy(alpha = 0.6F))
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "Settings Icon",
            tint = Color.Black.copy(alpha = .8F),
            modifier = Modifier
                .padding(8.dp)
                .size(24.dp)
        )
//        Spacer(modifier = Modifier.width(2.dp))
        SearchBar()
    }
}

@Composable
fun SearchBar() {
    val value by remember { mutableStateOf("Search artists, songs and playlists") }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White.copy(alpha = 0.5F),
                shape = RoundedCornerShape(39.dp)
            )
    ) {
        Icon(
            Icons.Default.Search,
            contentDescription = "search icon",
            tint = Color.DarkGray.copy(alpha = .6F),
            modifier = Modifier
                .padding(start = 8.dp)
                .size(12.dp)
        )
        Text(
            text = value,
            modifier = Modifier.padding(8.dp),
            color = Color.DarkGray.copy(alpha = .6f)

        )
    }

}

@Preview(showBackground = true)
@Composable
fun TopBarPrev() {
    TopBar()
}