package com.excu_fcd.plugin

import android.graphics.drawable.Drawable
import android.view.View

data class PluginMenuItem(
    val title: String,
    val icon: Drawable? = null,
    val onClick: (View) -> Unit = { },
    val onHold: (View) -> Unit = { }
) {
}