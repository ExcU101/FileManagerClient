package com.excu_fcd.filemanagerclient.mvvm.feature.worker.local

import androidx.documentfile.provider.DocumentFile
import com.excu_fcd.filemanagerclient.mvvm.data.local.LocalUriModel
import com.excu_fcd.filemanagerclient.mvvm.data.request.Request
import com.excu_fcd.filemanagerclient.mvvm.data.request.type.CreateOperationType
import com.excu_fcd.filemanagerclient.mvvm.feature.LocalEventPack
import com.excu_fcd.filemanagerclient.mvvm.feature.worker.Worker
import com.excu_fcd.filemanagerclient.mvvm.feature.worker.result.Result
import java.io.File

class CreateWorker : Worker<LocalUriModel, LocalEventPack> {

    override fun getName(): String {
        return "Create Worker"
    }

    override suspend fun work(
        request: Request<LocalUriModel>,
        onItemResult: (LocalEventPack) -> Unit,
    ) {
        val operations = request.getOperations()
        operations.forEach { operation ->
            if (operation.type is CreateOperationType) {
                operation.data.forEach {
                    val result = getResult(file = it.getFile(), isDirectory = it.isDirectory())
                    if (result == Result.success()) {
                        request.updateProgress(1)
                    }
                    onItemResult(LocalEventPack(operation.type, it, result))
                }
            }
        }
    }

    private fun getResult(file: DocumentFile, isDirectory: Boolean): Result {
        return if (isDirectory) {
            createFolder(file = file)
        } else {
            createFile(file = file)
        }
    }

    private fun createFolder(file: DocumentFile): Result {
        if (file.exists()) return Result.failure()

        return if (file.createDirectory(file.name!!) != null) Result.success() else Result.failure()
    }

    private fun createFile(file: DocumentFile): Result {
        if (file.exists()) return Result.failure()

        return if (file.createFile(file.type!!,
                file.name!!) != null
        ) Result.success() else Result.failure()
    }
}