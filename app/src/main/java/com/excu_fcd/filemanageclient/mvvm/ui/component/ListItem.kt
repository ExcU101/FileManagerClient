package com.excu_fcd.filemanageclient.mvvm.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun OneLineItem(
    modifier: Modifier = Modifier,
    text: String,
    icon: Painter? = null,
    iconClick: () -> Unit = {},
) {
    Row(modifier = modifier
        .fillMaxSize()
        .clickable {

        }
        .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center) {
        Text(text = text, modifier = Modifier.weight(1F))
        icon?.let {
            IconButton(onClick = iconClick, modifier = Modifier
                .weight(1F)
                .wrapContentWidth(Alignment.End)) {
                Icon(painter = it, contentDescription = null)
            }
        }
    }
}

@Composable
fun OneLineItem(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector? = null,
    iconClick: () -> Unit = {},
) {
    Row(modifier = modifier
        .fillMaxSize()
        .clickable {

        }
        .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center) {
        Text(text = text, modifier = Modifier.weight(1F))
        icon?.let {
            IconButton(onClick = iconClick, modifier = Modifier
                .weight(1F)
                .wrapContentWidth(Alignment.End)) {
                Icon(imageVector = it, contentDescription = null)
            }
        }
    }

}