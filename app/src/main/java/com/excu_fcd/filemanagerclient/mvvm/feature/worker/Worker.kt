package com.excu_fcd.filemanagerclient.mvvm.feature.worker

import com.excu_fcd.filemanagerclient.mvvm.data.Nameable
import com.excu_fcd.filemanagerclient.mvvm.data.local.LocalUriModel
import com.excu_fcd.filemanagerclient.mvvm.data.request.Request
import com.excu_fcd.filemanagerclient.mvvm.feature.worker.result.Result

interface Worker<I> : Nameable {

    suspend fun confirm(request: Request<I>): Boolean {
        if (request.getOperations().isEmpty()) return false
        if (request.getProgress() == request.getOperations().size) return false
        return true
    }

    fun onSuccess(block: () -> Unit) {
        block()
    }

    fun onFailure(block: () -> Unit) {
        block()
    }

    suspend fun work(
        request: Request<LocalUriModel>,
        onResponse: (result: Result) -> Unit,
        onFullSuccess: (Result) -> Unit = {},
    )
}