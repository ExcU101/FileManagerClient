package com.excu_fcd.filemanagerclient.mvvm.feature.worker

import com.excu_fcd.filemanagerclient.mvvm.data.request.Request
import com.excu_fcd.filemanagerclient.mvvm.feature.worker.result.Result

class WorkerImpl : Worker<String>() {

    override suspend fun confirm(request: Request<String>): Boolean {
        return false
    }

    override fun getName(): String {
        return "Worker Impl"
    }

    override suspend fun work(request: Request<String>, block: (it: Result) -> Unit): Result {
        return Result.failure()
    }
}