package com.excu_fcd.filemanagerclient.mvvm.ui.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SimpleToolbar(
    modifier: Modifier = Modifier,
    titleText: String,
    backgroundColor: Color = Color.White,
    contentColor: Color = Color.Black,
    navigationIconClick: () -> Unit = { },
    actions: @Composable RowScope.() -> Unit = { },
) {
    TopAppBar(
        modifier = modifier,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        navigationIcon = {
            ClickableIcon(icon = Icons.Outlined.Menu, onClick = navigationIconClick)
        },
        title = {
            Text(text = titleText)
        },
        actions = actions
    )
}

@Composable
fun SimpleToolbar(
    modifier: Modifier = Modifier,
    titleText: String,
    backgroundColor: Color = Color.White,
    contentColor: Color = Color.Black,
    navigationIconClick: () -> Unit = { },
    actions: List<Action>,
) {
    TopAppBar(
        modifier = modifier,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        elevation = 0.dp,
        navigationIcon = {
            ClickableIcon(icon = Icons.Outlined.Menu, onClick = navigationIconClick)
        },
        title = {
            Text(text = titleText)
        },
        actions = {
            actions.forEach {
                ClickableIcon(icon = it.icon, onClick = it.onClick, contentDescription = it.name)
            }
        }
    )
}