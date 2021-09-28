package com.excu_fcd.filemanagerclient.mvvm.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RoundedFilledIcon(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    contentDescription: String? = null,
    shape: Shape = RoundedCornerShape(percent = 100),
    backgroundTint: Color = Color.Black,
    requireSize: Dp = 36.dp,
    tint: Color = Color.White,
) {
    Box(
        modifier = modifier
            .requiredSize(requireSize)
            .background(color = backgroundTint, shape = shape)
            .paint(rememberVectorPainter(image = imageVector),
                colorFilter = ColorFilter.tint(color = tint), contentScale = ContentScale.Inside),
    )
}

@Composable
fun RoundedFilledIcon(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String? = null,
    shape: Shape = RoundedCornerShape(percent = 100),
    backgroundTint: Color = Color.Black,
    tint: Color = Color.White,
) {
    Icon(painter = painter,
        contentDescription = contentDescription,
        modifier = modifier.background(color = backgroundTint, shape = shape), tint = tint)
}

@Composable
fun ClickableIcon(
    modifier: Modifier = Modifier,
    icon: Painter,
    contentDescription: String? = null,
    onClick: () -> Unit,
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(painter = icon, contentDescription = contentDescription)
    }
}

@Composable
fun ClickableIcon(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    contentDescription: String? = null,
    onClick: () -> Unit,
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(imageVector = icon, contentDescription = contentDescription)
    }
}
