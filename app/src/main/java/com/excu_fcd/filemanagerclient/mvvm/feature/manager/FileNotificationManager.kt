package com.excu_fcd.filemanagerclient.mvvm.feature.manager

import android.content.Context
import androidx.documentfile.provider.DocumentFile
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