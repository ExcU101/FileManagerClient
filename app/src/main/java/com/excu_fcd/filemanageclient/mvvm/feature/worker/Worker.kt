package com.excu_fcd.filemanageclient.mvvm.feature.worker

import com.excu_fcd.filemanageclient.mvvm.data.Nameable
import com.excu_fcd.filemanageclient.mvvm.data.request.Request
import com.excu_fcd.filemanageclient.mvvm.feature.worker.result.Result

abstract class Worker<I> : Nameable {

    abstract suspend fun confirm(request: Request<I>): Boolean

    abstract suspend fun work(request: Request<I>, block: (it: Result) -> Unit): Result

    fun onSuccess(block: () -> Unit) {
        block()
    }

    fun onFailure(block: () -> Unit) {
        block()
    }

}