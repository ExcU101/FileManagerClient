package com.excu_fcd.filemanageclient.mvvm.feature.manager

import android.content.Context
import javax.inject.Inject

class FileNotificationManager @Inject constructor(private val context: Context) :
    ContextManager(context = context) {

    override fun getTag(): String {
        return ""
    }

    override fun getName(): String {
        return "Fuck"
    }

}