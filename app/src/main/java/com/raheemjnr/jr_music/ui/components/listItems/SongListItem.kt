package com.raheemjnr.jr_music.ui.components.listItems

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.raheemjnr.jr_music.utils.noRippleClickable
import com.raheemjnr.jr_music.utils.showToast

@Composable
fun SongListItem(
    songTitle: String = "Title of song",
    songArtist: String = "Artist",
    songAlbum: String = "Album",
    context: Context,
    onclick: () -> Unit

) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, top = 4.dp, bottom = 4.dp, end = 4.dp)
            .noRippleClickable {
                onclick()
            }
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(.7f)
        ) {
            //song art
            Column(
                modifier = Modifier.padding(end = 12.dp)
            ) {
                Image(
                    painterResource(id = R.drawable.music_logo),
                    contentDescription = "",
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.Center,
                    colorFilter = ColorFilter.tint(color = Color.White),
                    modifier = Modifier
                        .background(shape = RoundedCornerShape(4.dp), color = Color.Gray)
                        .padding(8.dp)
                        .size(30.dp)
                )
            }
            //column
            Column {
                //title
                Text(
                    text = songTitle,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                )
                //info row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.local_storage),
                        contentDescription = "",
                        modifier = Modifier
                            .size(18.dp)
                    )
                    Text(
                        text = songArtist,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W400,
                        fontFamily = FontFamily.Default
                    )
                    Text(
                        text = "|",
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W400,
                        fontFamily = FontFamily.Default,
                        modifier = Modifier.padding(4.dp)
                    )
                    Text(
                        text = songAlbum,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W400,
                        fontFamily = FontFamily.Default
                    )

                }
            }
        }
        //playing and morevert
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {

            Icon(
                painter = painterResource(id = R.drawable.muzic_playing),
                contentDescription = "indicate playing icon",
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(26.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Icon(
                painter = painterResource(id = R.drawable.morevert),
                contentDescription = "",
                modifier = Modifier
                    .noRippleClickable {
                        showToast(
                            context = context,
                            message = "clicked moreVert", Toast.LENGTH_SHORT
                        )
                    }
                    .padding(end = 8.dp)
                    .size(30.dp)
            )
        }


    }

}

@Preview(showBackground = true)
@Composable
fun SongListItemPreview() {
    val context = LocalContext.current
//    SongListItem(
//        songTitle = "Title of song",
//        songArtist = "Artist",
//        songAlbum = "Album",
//        context,
//        onclick = {}
//
//    )
}