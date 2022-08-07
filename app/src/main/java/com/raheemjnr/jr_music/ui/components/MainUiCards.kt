package com.raheemjnr.jr_music.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainUiCard() {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(8.dp)
    ) {
        MainCard(icon = Icons.Default.Favorite, "Favorite")
        Spacer(modifier = Modifier.width(4.dp))
        MainCard(icon = Icons.Default.Add, "Playlist")
        Spacer(modifier = Modifier.width(4.dp))
        MainCard(icon = Icons.Default.Share, "Recent")
    }

}

@Composable
fun MainCard(icon: ImageVector, text: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(
                color = Color.LightGray.copy(alpha = .25F),
                shape = RoundedCornerShape(6.dp)
            )
            .padding(start = 18.dp, end = 18.dp, top = 4.dp, bottom = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "card icon",
            tint = Color.Black.copy(alpha = .5f)
        )
        //
        Text(
            text = text,
            color = Color.DarkGray.copy(alpha = .6f),
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 1.dp, top = 4.dp, bottom = 4.dp),
        )

    }
}

@Preview(showBackground = true)
@Composable
fun MainCardPrev() {
    MainCard(icon = Icons.Default.Favorite, "Favorite")
}

@Preview(showBackground = true)
@Composable
fun MainUiCardPrev() {
    MainUiCard()
}