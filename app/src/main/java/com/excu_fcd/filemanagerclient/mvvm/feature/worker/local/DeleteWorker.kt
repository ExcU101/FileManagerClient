package com.excu_fcd.filemanagerclient.mvvm.feature.worker.local

import com.excu_fcd.filemanagerclient.mvvm.data.local.LocalUriModel
import com.excu_fcd.filemanagerclient.mvvm.data.request.Request
import com.excu_fcd.filemanagerclient.mvvm.data.request.type.DeleteOperationType
import com.excu_fcd.filemanagerclient.mvvm.feature.worker.Worker
import com.excu_fcd.filemanagerclient.mvvm.feature.worker.result.Result
import java.io.File

class DeleteWorker : Worker<LocalUriModel> {

    override fun getName(): String {
        return "Delete worker"
    }

    override suspend fun work(
        request: Request<LocalUriModel>,
        onResponse: (result: Result) -> Unit,
        onFullSuccess: (Result) -> Unit,
    ) {
        val operations = request.getOperations()
        operations.forEach { operation ->
            if (operation.type is DeleteOperationType) {
                operation.data.forEach {
                    val result = getResult(file = it.getFile())
                    if (result == Result.success()) {
                        request.updateProgress(1)
                    }
                    onResponse(result)
                }
            }
        }
    }

    private fun getResult(file: File): Result {
        if (file.exists()) {
            if (file.deleteRecursively()) {
                return Result.success()
            }
            return Result.failure()
        }
        return Result.failure()
    }
}