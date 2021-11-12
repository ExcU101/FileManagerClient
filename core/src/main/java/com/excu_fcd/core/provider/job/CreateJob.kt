package com.excu_fcd.core.provider.job

import android.content.Context
import com.excu_fcd.core.data.model.DocumentModel
import com.excu_fcd.core.data.request.operation.Operation
import com.excu_fcd.core.data.request.operation.reason.Reason
import com.excu_fcd.core.extensions.asDocumentModel
import com.excu_fcd.core.extensions.logIt
import com.excu_fcd.core.provider.job.callback.ModelJobCallback
import com.excu_fcd.core.provider.job.callback.OperationJobCallback
import java.io.File

object CreateJob : Job {

    override fun getType(): Operation.Type {
        return Operation.Type.create()
    }

    override suspend fun doWork(
        operation: Operation,
        operationCallback: OperationJobCallback?,
        itemOperationCallback: ModelJobCallback?,
    ) {
        if (operation.data.isEmpty()) {
            operationCallback?.onDenied(
                operation = operation,
                reason = Reason.operationEmptyData()
            )
            return
        }

        if (operation.type == getType()) {
            operationCallback?.onGranted(operation = operation, reason = Reason.empty())
            operation.data.forEach {
                create(file = it, operation = operation, callback = itemOperationCallback)
            }
        } else {
            operationCallback?.onDenied(
                operation = operation,
                reason = Reason.text("Error type (${javaClass.simpleName})")
            )
        }
    }

    private fun create(
        file: DocumentModel,
        operation: Operation,
        callback: ModelJobCallback? = null,
    ) {
        val parent = File(file.getPathParent()).asDocumentModel()

        parent.run {
            if (exists()) {
                if (!file.exists()) {
                    if (createInside(file.isDirectory(), file).logIt()) {
                        callback?.onSuccess(operation, file)
                    } else {
                        callback?.onFailure(
                            operation = operation,
                            item = file,
                            reason = Reason.text("Something gone wrong")
                        )
                    }
                } else {
                    callback?.onFailure(
                        operation = operation,
                        item = file,
                        reason = Reason.text("Already exists")
                    )
                }
            } else {
                callback?.onFailure(
                    operation = operation,
                    item = file,
                    reason = Reason.text("Parent doesn't exist!")
                )
            }
        }
    }

}