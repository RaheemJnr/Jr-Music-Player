package com.raheemjnr.jr_music.ui.components.localScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raheemjnr.jr_music.R
import com.raheemjnr.jr_music.utils.getGreeting

@Composable
fun MainUiCard() {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(color = Color.LightGray.copy(alpha = .25F))
            .padding(top = 2.dp, bottom = 22.dp)
    ) {
        Column {
            GreetingText(text = getGreeting())
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                MainCard(icon = painterResource(id = R.drawable.card_favorite), "Favorite")
                MainCard(icon = painterResource(id = R.drawable.card_playlist), "Playlist")
                MainCard(icon = painterResource(id = R.drawable.card_recent), "Recent")
            }
        }
    }

}

@Composable
fun GreetingText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = typography.h5.copy(
            fontWeight = FontWeight.ExtraBold,
            color = Color.DarkGray.copy(alpha = .6f)
        ),
        modifier = modifier.padding(start = 14.dp, end = 4.dp, bottom = 8.dp)
    )
}

@Composable
fun MainCard(icon: Painter, text: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(
                color = Color.White,
                shape = RoundedCornerShape(6.dp)
            )
            .padding(start = 40.dp, end = 40.dp, top = 16.dp, bottom = 12.dp)
    ) {
        Icon(
            painter =  icon,
            contentDescription = "card icon",
            tint = Color.Black.copy(alpha = .5f),
            modifier = Modifier.size(28.dp)
        )
        //
        Text(
            text = text,
            color = Color.DarkGray.copy(alpha = .6f),
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 1.dp, top = 4.dp, bottom = 4.dp),
        )

    }
}

@Preview(showBackground = true)
@Composable
fun MainCardPrev() {
    MainCard(icon = painterResource(id = R.drawable.card_favorite), "Favorite")
}

@Preview(showBackground = true)
@Composable
fun MainUiCardPrev() {
    MainUiCard()
}