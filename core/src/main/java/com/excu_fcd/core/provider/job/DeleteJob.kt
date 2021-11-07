package com.excu_fcd.core.provider.job

import com.excu_fcd.core.data.model.DocumentModel
import com.excu_fcd.core.data.request.operation.Operation
import com.excu_fcd.core.data.request.operation.reason.Reason
import com.excu_fcd.core.provider.job.callback.ModelJobCallback
import com.excu_fcd.core.provider.job.callback.OperationJobCallback

object DeleteJob : Job {

    override fun getType(): Operation.Type {
        return Operation.Type.delete()
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
            operationCallback?.onGranted(operation = operation)
            operation.data.forEach {
                delete(file = it, operation = operation, callback = itemOperationCallback)
            }
        } else {
            operationCallback?.onDenied(
                operation = operation,
                Reason.text("Error type (${javaClass.simpleName})")
            )
        }
    }

    private fun delete(
        operation: Operation,
        file: DocumentModel,
        callback: ModelJobCallback? = null,
    ) {
        if (file.exists()) {
            if (file.delete()) {
                callback?.onSuccess(
                    operation = operation, item = file,
                    reason = Reason.text("${file.getName()} successfully deleted")
                )
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
                reason = Reason.text("${file.getName()} doesn't exist")
            )
        }
    }

}