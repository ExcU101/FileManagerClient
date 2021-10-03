package com.excu_fcd.filemanagerclient.mvvm.feature.manager.task

import android.content.Context
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.ContextManager
import javax.inject.Inject

class TaskManager @Inject constructor(val context: Context) : ContextManager(context = context) {
    override fun getTag(): String {
        return "TASK_MANAGER"
    }

    override fun getName(): String {
        return "Task Manager"
    }
}