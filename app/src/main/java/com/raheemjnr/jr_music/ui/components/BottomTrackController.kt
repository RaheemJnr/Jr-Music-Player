package com.raheemjnr.jr_music.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raheemjnr.jr_music.R
import com.raheemjnr.jr_music.ui.theme.spotifyGray

@Composable
fun BottomTrackController(
    trackName: String,
    seekState: Float,
    imageUrl: String,
    nowPlayingClicked: () -> Unit,
    artistName: String,
    onChangePlayerClicked: () -> Unit,
    hasLiked: Boolean,
    onLikeClicked: () -> Unit
) {
    //Player
    if (trackName.isNotEmpty()) {
        Column(
            modifier = Modifier
                .padding(horizontal = 6.dp)
                .background(
                    Color.White,
                    shape = RoundedCornerShape(
                        topStart = 10.dp,
                        topEnd = 10.dp,
                        bottomEnd = 6.dp,
                        bottomStart = 6.dp
                    )
                )
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(
                        Color.White,
                        shape = RoundedCornerShape(
                            topStart = 10.dp,
                            topEnd = 10.dp,
                            bottomEnd = 10.dp,
                            bottomStart = 10.dp
                        )
                    )

            ) {

                Image(
                    painter = painterResource(id = R.drawable.music_logo),
                    contentDescription = "BottomBarPlayer",
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(60.dp),
                )

                Column(
                    Modifier
                        .padding(start = 10.dp)
                        .align(Alignment.CenterVertically)
                        .clickable {
                            nowPlayingClicked()
                        }
                        .fillMaxWidth(0.7f)
                ) {
                    Text(
                        text = trackName,
                        color = spotifyGray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        maxLines = 1
                    )
                    Text(
                        text = artistName,
                        color = Color.Gray,
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp
                    )
                }
                Box(
                    contentAlignment = Alignment.CenterEnd,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .width(150.dp)
                            .padding(end = 10.dp)
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.play_button), //else Icons.Default.PlayArrow,
                            contentDescription = "Like",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
//                                    if (isPlaying)
//                                        isPlaying
//                                    else isPlaying

                                },
                            colorFilter = ColorFilter.tint(spotifyGray),
                        )
                    }

                }
            }
            Box(
                Modifier
                    .height(3.dp)
                    .padding(start = 12.dp, end = 12.dp, bottom = 1.dp)
                    .fillMaxWidth()
                    .background(
                        spotifyGray,
                        shape = RoundedCornerShape(2.dp)
                    )

            ) {
                Row(
                    Modifier
                        .fillMaxHeight()
                        .background(
                            Color.Green, shape = RoundedCornerShape(2.dp)
                        )
                        .fillMaxWidth(1F)
                        .animateContentSize()
                ) {}
                Row(
                    Modifier
                        .fillMaxHeight()
                        .background(
                            spotifyGray, shape = RoundedCornerShape(2.dp)
                        )
                        .fillMaxWidth(seekState)
                        .animateContentSize()
                ) {}

            }
        }

    }
}