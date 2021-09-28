package com.excu_fcd.filemanagerclient.mvvm.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun OneLineItem(
    modifier: Modifier = Modifier,
    text: String,
    painter: Painter? = null,
    iconClick: () -> Unit = {},
    tint: Color = Color.Black
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.White
    ) {
        Row(modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {
            Text(text = text, modifier = Modifier.weight(1F))
            painter?.let {
                IconButton(onClick = iconClick, modifier = Modifier
                    .weight(1F)
                    .wrapContentWidth(Alignment.End)) {
                    Icon(painter = it, contentDescription = null, tint = tint)
                }
            }
        }
    }

}

@Composable
fun OneLineItem(
    modifier: Modifier = Modifier,
    text: String,
    imageVector: ImageVector? = null,
    iconClick: () -> Unit = {},
    tint: Color = Color.Black,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.White
    ) {
        Row(modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {
            Text(text = text, modifier = Modifier.weight(1F))
            imageVector?.let {
                IconButton(onClick = iconClick, modifier = Modifier
                    .weight(1F)
                    .wrapContentWidth(Alignment.End)) {
                    Icon(imageVector = it, contentDescription = null, tint = tint)
                }
            }
        }
    }
}