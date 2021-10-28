package com.excu_fcd.core.provider.job

import com.excu_fcd.core.data.model.DocumentModel
import com.excu_fcd.core.data.request.operation.Operation
import com.excu_fcd.core.data.request.operation.reason.Reason

object DeleteJob : Job {

    override fun getType(): Operation.Type {
        return Operation.Type.delete()
    }

    override suspend fun doWork(
        operation: Operation,
        operationCallback: Job.OperationCallback?,
        itemOperationCallback: Job.ItemOperationCallback?,
    ) {
        if (operation.data.isEmpty()) {
            operationCallback?.onOperationTypeDenied(operation = operation,
                reason = Reason.operationEmptyData())
            return
        }

        if (operation.type == getType()) {
            operationCallback?.onOperationTypeGranted(operation = operation)
            operation.data.forEach {
                delete(file = it, callback = itemOperationCallback)
            }
        } else {
            operationCallback?.onOperationTypeDenied(operation = operation,
                Reason.text("Error type"))
        }
    }

    private fun delete(file: DocumentModel, callback: Job.ItemOperationCallback? = null) {
        if (file.exists()) {
            if (file.delete()) {
                callback?.onItemOperationSuccess(file,
                    "${file.getName()} successfully deleted")
            } else {
                callback?.onItemOperationFailure(file, "Something gone wrong")
            }
        } else {
            callback?.onItemOperationFailure(file, "${file.getName()} doesn't exist")
        }
    }

}