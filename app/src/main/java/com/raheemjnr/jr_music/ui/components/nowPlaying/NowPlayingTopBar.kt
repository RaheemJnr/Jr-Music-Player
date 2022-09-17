package com.raheemjnr.jr_music.ui.components.nowPlaying

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raheemjnr.jr_music.R
import java.util.*

@Composable
fun NowPlayingTopBar(
    albumName: String?,
    dropDown: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .fillMaxHeight(0.08f),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.dropdown_arror),
            contentDescription = "",
            modifier = Modifier
                .size(18.dp)
                .clickable {
                    dropDown()
                },
            tint = Color.White
        )

        Column(Modifier.weight(0.8f)) {
            Text(
                text = "PLAYING FROM PLAYLIST".uppercase(Locale.US),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                fontSize = 10.sp
            )
            Text(
                text = albumName ?: "",
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold

            )
        }
        Icon(
            painter = painterResource(id = R.drawable.morevert),
            tint = Color.White,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
        )

    }
}