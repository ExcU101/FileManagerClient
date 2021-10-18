package com.excu_fcd.filemanagerclient.mvvm.feature.manager

import android.content.Context
import com.excu_fcd.filemanagerclient.mvvm.data.request.Request
import com.excu_fcd.filemanagerclient.mvvm.feature.EventPack

abstract class EmployerManager<I, R: EventPack> constructor(context: Context) :
    ContextManager(context = context) {

    abstract suspend fun sendRequest(request: Request<I>, onResponse: (R) -> Unit = {})

}