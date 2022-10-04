package com.raheemjnr.jr_music.ui.components.localScreen

import android.content.Context
import androidx.compose.runtime.Composable
import com.raheemjnr.jr_music.data.model.Songs
import com.raheemjnr.jr_music.ui.components.listItems.SongListItem

@Composable
fun SongListScreen(
    context: Context,
    item: Songs
) {

    SongListItem(
        songTitle = item.title,
        songArtist = item.artist,
        songAlbum = item.album,
        context = context
    ) {
    }
}