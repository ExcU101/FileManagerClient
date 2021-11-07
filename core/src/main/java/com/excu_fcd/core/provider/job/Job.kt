package com.excu_fcd.core.provider.job

import com.excu_fcd.core.data.model.DocumentModel
import com.excu_fcd.core.data.request.operation.Operation
import com.excu_fcd.core.data.request.operation.reason.EmptyReason
import com.excu_fcd.core.data.request.operation.reason.Reason
import com.excu_fcd.core.data.request.operation.reason.TextualReason
import com.excu_fcd.core.provider.job.callback.ModelJobCallback
import com.excu_fcd.core.provider.job.callback.OperationJobCallback

interface Job {

    fun getType(): Operation.Type

    suspend fun doWork(
        operation: Operation,
        operationCallback: OperationJobCallback? = null,
        itemOperationCallback: ModelJobCallback? = null,
    )

    interface OperationCallback {

        fun onOperationTypeGranted(operation: Operation)

        fun onOperationTypeDenied(operation: Operation, reason: Reason)

    }

}

fun ModelJobCallback.onSuccess(
    operation: Operation,
    model: DocumentModel
) =
    onSuccess(operation = operation, item = model, reason = EmptyReason)

fun ModelJobCallback.onFailure(
    operation: Operation,
    model: DocumentModel
) =
    onFailure(operation = operation, item = model, reason = EmptyReason)

fun ModelJobCallback.onSuccess(
    operation: Operation,
    model: DocumentModel,
    reason: String,
) =
    onSuccess(operation = operation, item = model, reason = TextualReason(text = reason))

fun ModelJobCallback.onFailure(
    operation: Operation,
    model: DocumentModel,
    reason: String,
) =
    onFailure(operation = operation, item = model, reason = TextualReason(text = reason))