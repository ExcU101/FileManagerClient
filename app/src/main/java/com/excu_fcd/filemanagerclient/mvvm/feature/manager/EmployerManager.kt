package com.excu_fcd.filemanagerclient.mvvm.feature.manager

import android.content.Context
import com.excu_fcd.filemanagerclient.mvvm.data.request.Request
import com.excu_fcd.filemanagerclient.mvvm.feature.worker.result.Result

abstract class EmployerManager<I> constructor(private val context: Context) :
    ContextManager(context = context) {

    abstract suspend fun sendRequest(request: Request<I>, onResponse: (result: Result) -> Unit = {})

}