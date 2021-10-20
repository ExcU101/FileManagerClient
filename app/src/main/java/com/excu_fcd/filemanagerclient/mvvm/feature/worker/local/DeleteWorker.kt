package com.excu_fcd.filemanagerclient.mvvm.feature.worker.local

import androidx.documentfile.provider.DocumentFile
import com.excu_fcd.filemanagerclient.mvvm.data.local.LocalUriModel
import com.excu_fcd.filemanagerclient.mvvm.data.request.Request
import com.excu_fcd.filemanagerclient.mvvm.data.request.type.DeleteOperationType
import com.excu_fcd.filemanagerclient.mvvm.feature.LocalEventPack
import com.excu_fcd.filemanagerclient.mvvm.feature.worker.Worker
import com.excu_fcd.filemanagerclient.mvvm.feature.worker.result.Result
import com.excu_fcd.filemanagerclient.mvvm.utils.isSuccess
import com.excu_fcd.filemanagerclient.mvvm.utils.logIt
import java.io.File

class DeleteWorker : Worker<LocalUriModel, LocalEventPack> {

    override fun getName(): String {
        return "Delete worker"
    }

    override suspend fun work(
        request: Request<LocalUriModel>,
        onItemResult: (LocalEventPack) -> Unit,
    ) {
        getName().logIt()
        val operations = request.getOperations()
        operations.forEach { operation ->
            if (operation.type is DeleteOperationType) {
                operation.data.forEach {
                    val result = getResult(it.getFile())
                    if (result.isSuccess()) {
                        request.updateProgress(1)
                    }
                    onItemResult(LocalEventPack(operation.type, it, result))
                }
            }
        }
    }

    private fun getResult(file: DocumentFile): Result {
        if (file.exists()) {
            if (file.delete().logIt()) {
                return Result.success()
            }
            return Result.failure()
        }
        return Result.failure().logIt()
    }
}