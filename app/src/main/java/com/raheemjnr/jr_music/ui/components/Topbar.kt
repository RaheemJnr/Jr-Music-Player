package com.raheemjnr.jr_music.ui.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raheemjnr.jr_music.utils.showToast

@Composable
fun CustomTopBar(context: Context) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.LightGray.copy(alpha = .25F))
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "Settings Icon",
            tint = Color.Black.copy(alpha = .9F),
            modifier = Modifier
                .padding(8.dp)
                .size(24.dp)
                .clickable { showToast(context = context, "Clicked settings", Toast.LENGTH_SHORT) }
        )
        SearchBar()
    }
}

@Composable
fun SearchBar() {
    val value by remember { mutableStateOf("Search artists, songs and playlists") }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .padding(start = 0.dp, end = 14.dp, top = 8.dp, bottom = 8.dp)
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(39.dp)
            )
    ) {
        //text field
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .height(IntrinsicSize.Min)

        ) {
            Icon(
                Icons.Default.Search,
                contentDescription = "search icon",
                tint = Color.DarkGray.copy(alpha = .6F),
                modifier = Modifier
                    .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                    .size(24.dp)
            )
            Text(
                text = value,
                modifier = Modifier.padding(start = 1.dp, top = 8.dp, bottom = 8.dp),
                color = Color.DarkGray.copy(alpha = .6f),
                fontSize = 12.sp,
                letterSpacing = .5.sp
            )
        }
        //mic
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .wrapContentSize()
                .padding(end = 12.dp)
        ) {
            Divider(
                color = Color.DarkGray.copy(alpha = .6F),
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .padding(top = 10.dp, bottom = 10.dp)
            )
            Icon(
                Icons.Default.Phone,
                contentDescription = "",
                tint = Color.DarkGray.copy(alpha = .6F),
                modifier = Modifier
                    .padding(start = 6.dp)
                    .size(22.dp)
            )


        }
    }


}

@Preview(showBackground = true)
@Composable
fun TopBarPrev() {

    CustomTopBar(LocalContext.current)
}