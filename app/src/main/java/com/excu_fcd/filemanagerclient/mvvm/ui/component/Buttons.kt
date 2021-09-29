package com.excu_fcd.filemanagerclient.mvvm.ui.component

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun ThemedIconFab(
    contentColor: Color = Color.Black,
    backgroundColor: Color = Color.White,
    icon: ImageVector = Icons.Outlined.Add,
    contentDescription: String? = null,
    onClick: () -> Unit = { },
) {
    FloatingActionButton(onClick = onClick,
        contentColor = contentColor,
        backgroundColor = backgroundColor) {
        Icon(imageVector = icon, contentDescription = contentDescription)
    }
}
