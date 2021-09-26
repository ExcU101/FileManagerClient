package com.excu_fcd.filemanageclient.mvvm.feature.manager

import android.content.Context
import com.excu_fcd.filemanageclient.mvvm.data.request.Request

abstract class EmployerManager<I> constructor(private val context: Context) :
    ContextManager(context = context) {

    abstract suspend fun sendRequest(request: Request<I>)
}