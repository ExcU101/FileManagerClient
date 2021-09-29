package com.excu_fcd.filemanagerclient.mvvm.ui.component

import androidx.compose.ui.graphics.painter.Painter

data class Action(val name: String, val icon: Painter, val onClick: () -> Unit = { })