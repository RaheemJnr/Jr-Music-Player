package com.raheemjnr.jr_music.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TopBar() {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "Settings Icon",
            modifier = Modifier
                .padding(8.dp)
                .size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        SearchBar()
    }
}


@Composable
fun SearchBar() {
    var value by remember { mutableStateOf("") }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        BasicTextField(
            value = value,
            onValueChange = { value = it }
        )
    }

}

@Preview(showBackground = true)
@Composable
fun TopBarPrev() {
    TopBar()
}