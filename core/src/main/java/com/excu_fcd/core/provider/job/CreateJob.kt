package com.excu_fcd.core.provider.job

import com.excu_fcd.core.data.model.DocumentModel
import com.excu_fcd.core.data.request.operation.Operation
import com.excu_fcd.core.data.request.operation.reason.Reason
import com.excu_fcd.core.extensions.asDocumentModel

object CreateJob : Job {

    override fun getType(): Operation.Type {
        return Operation.Type.create()
    }

    override suspend fun doWork(
        operation: Operation,
        operationCallback: Job.OperationCallback?,
        itemOperationCallback: Job.ItemOperationCallback?,
    ) {
        if (operation.data.isEmpty() || operation.data.count() < 2) {
            operationCallback?.onOperationTypeDenied(operation,
                Reason.operationEmptyData())
            return
        }

        if (operation.type == getType()) {
            operationCallback?.onOperationTypeGranted(operation = operation)
            operation.data.forEach {
                create(file = it, callback = itemOperationCallback)
            }
        } else {
            operationCallback?.onOperationTypeDenied(operation = operation,
                Reason.text("Error type"))
        }
    }

    private fun create(
        file: DocumentModel,
        callback: Job.ItemOperationCallback? = null,
    ) {
        val parent = file.getParent()?.asDocumentModel()

        if (parent != null) {
            if (parent.exists()) {
                if (!file.exists()) {
                    if (parent.createInside(file.isDirectory())) {
                        callback?.onItemOperationSuccess(file)
                    } else {
                        callback?.onItemOperationFailure(file, "Something gone wrong")
                    }
                }
            } else {
                callback?.onItemOperationFailure(parent, "Parent doesn't exist!")
            }
        }

    }

}