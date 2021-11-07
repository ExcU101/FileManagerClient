package com.excu_fcd.core.provider.job

import com.excu_fcd.core.data.model.DocumentModel
import com.excu_fcd.core.data.request.operation.Operation
import com.excu_fcd.core.data.request.operation.reason.Reason
import com.excu_fcd.core.provider.job.callback.ModelJobCallback
import com.excu_fcd.core.provider.job.callback.OperationJobCallback
import com.excu_fcd.core.provider.job.callback.RenameModelJobCallback

object RenameJob : Job {

    override fun getType(): Operation.Type {
        return Operation.Type.rename()
    }

    override suspend fun doWork(
        operation: Operation,
        operationCallback: OperationJobCallback?,
        itemOperationCallback: ModelJobCallback?,
    ) {
        if (operation.data.isEmpty() || operation.data.size < 2) {
            operationCallback?.onDenied(
                operation = operation,
                reason = Reason.operationEmptyData()
            )
            return
        }

        if (operation.type == getType()) {
            operationCallback?.onGranted(operation = operation, reason = Reason.empty())
            val last = operation.data.last()

            operation.data.forEach {
                if (it != last) {
                    rename(
                        model = it,
                        dest = last,
                        operation = operation,
                        callback = itemOperationCallback as RenameModelJobCallback?
                    )
                }
            }
        } else {
            operationCallback?.onDenied(
                operation = operation,
                reason = Reason.text("Error type (${javaClass.simpleName})")
            )
        }
    }

    private fun rename(
        operation: Operation,
        model: DocumentModel,
        dest: DocumentModel,
        callback: RenameModelJobCallback? = null,
    ) {
        if (model.exists()) {
            if (model.rename(dest)) {
                callback?.onRenameSuccess(
                    operation = operation,
                    oldModel = model,
                    destModel = dest,
                    reason = Reason.text("Fuck")
                )
            } else {
                callback?.onRenameFailure(
                    operation,
                    oldModel = model,
                    destModel = dest,
                    reason = Reason.text("Something gone wrong")
                )
            }
        } else {
            callback?.onRenameFailure(
                operation = operation,
                oldModel = model,
                destModel = dest,
                reason = Reason.text("Doesn't exist")
            )
        }
    }

}