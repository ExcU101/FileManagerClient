package com.excu_fcd.filemanagerclient.mvvm.feature.worker

import com.excu_fcd.filemanagerclient.mvvm.data.Nameable
import com.excu_fcd.filemanagerclient.mvvm.data.request.Request

interface Worker<I, R> : Nameable {

    suspend fun confirm(request: Request<I>): Boolean {
        if (request.getOperations().isEmpty()) return false
        if (request.getProgress() == request.getOperations().size) return false
        return true
    }

    suspend fun work(
        request: Request<I>,
        onItemResult: (R) -> Unit,
    )

}