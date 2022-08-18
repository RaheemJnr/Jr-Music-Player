package com.raheemjnr.jr_music.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SongListItem() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        //image

        Image(
            Icons.Default.Home,
            contentDescription = "",
            contentScale = ContentScale.Fit,
            alignment = Alignment.Center,
            modifier = Modifier
                .background(shape = RoundedCornerShape(4.dp), color = Color.Green)
                .padding(8.dp)
                .size(30.dp)
        )

        //column
        Column(
            modifier = Modifier.fillMaxWidth(.8f)
        ) {
            Text(
                text = "Title of song",
                overflow = TextOverflow.Ellipsis,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(12.dp)
                )
                Text(
                    text = "Other songs details",
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 8.sp
                )

            }
        }
        Row {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = ""
            )
            Icon(
                imageVector = Icons.Default.MoreVert,
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