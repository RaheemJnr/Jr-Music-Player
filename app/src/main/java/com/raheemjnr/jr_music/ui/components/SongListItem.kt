package com.raheemjnr.jr_music.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SongListItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .background(shape = RoundedCornerShape(4.dp), color = Color.Green)
                .padding(8.dp)
        ) {
            Image(
                Icons.Default.Home,
                contentDescription = "",
                modifier = Modifier
            )
        }

    }

}

@Preview(showBackground = true)
@Composable
fun SongListItemPreview() {
    SongListItem()
}