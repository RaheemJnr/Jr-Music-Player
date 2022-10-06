package com.raheemjnr.jr_music.ui.components.listItems

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raheemjnr.jr_music.R
import com.raheemjnr.jr_music.utils.showToast

@Composable
fun AlbumsItem(
    albumTitle: String = "Album Title",
    albumArtist: String = "Artist",
    context: Context,
    onclick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, top = 4.dp, bottom = 4.dp, end = 4.dp)
            .clickable {
                onclick()
            }
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(.8f)
        ) {
            Column(
                modifier = Modifier.padding(end = 12.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.music_logo),
                    contentDescription = "",
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.Center,
                    colorFilter = ColorFilter.tint(color = Color.White),
                    modifier = Modifier
                        .background(
                            shape = RoundedCornerShape(4.dp),
                            color = Color.Gray
                        )
                        .padding(8.dp)
                        .size(30.dp)
                )
            }

            //column
            Column(
                modifier = Modifier.fillMaxWidth(.75f)
            ) {
                Text(
                    text = albumTitle,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = albumArtist,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W400,
                        fontFamily = FontFamily.Default
                    )
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painterResource(id = R.drawable.morevert),
                contentDescription = "",
                modifier = Modifier
                    .clickable {
                        showToast(
                            context = context,
                            message = "clicked moreVert", Toast.LENGTH_SHORT
                        )
                    }
                    .padding(end = 8.dp)
                    .size(28.dp)
            )
        }


    }
}

@Preview(showBackground = true)
@Composable
fun AlbumListPreview() {
    AlbumsItem(context = LocalContext.current) {

    }
}