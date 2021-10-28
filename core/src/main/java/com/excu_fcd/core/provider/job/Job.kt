package com.excu_fcd.core.provider.job

import com.excu_fcd.core.data.model.DocumentModel
import com.excu_fcd.core.data.request.operation.Operation
import com.excu_fcd.core.data.request.operation.reason.Reason
import com.excu_fcd.core.data.request.operation.reason.TextualReason

interface Job {

    fun getType(): Operation.Type

    suspend fun doWork(
        operation: Operation,
        operationCallback: OperationCallback? = null,
        itemOperationCallback: ItemOperationCallback? = null,
    )

    interface OperationCallback {

        fun onOperationTypeGranted(operation: Operation)

        fun onOperationTypeDenied(operation: Operation, reason: Reason)

    }

    interface ItemOperationCallback {

        fun onOperationWork(operation: Operation)

        fun onItemOperationSuccess(model: DocumentModel, reason: Reason)

        fun onItemOperationFailure(model: DocumentModel, reason: Reason)

    }

}

fun Job.ItemOperationCallback.onItemOperationSuccess(model: DocumentModel) =
    onItemOperationSuccess(model = model, reason = Reason.empty())

fun Job.ItemOperationCallback.onItemOperationFailure(model: DocumentModel) =
    onItemOperationFailure(model = model, reason = Reason.empty())

fun Job.ItemOperationCallback.onItemOperationSuccess(model: DocumentModel, reason: String) =
    onItemOperationSuccess(model, TextualReason(text = reason))

fun Job.ItemOperationCallback.onItemOperationFailure(model: DocumentModel, reason: String) =
    onItemOperationFailure(model, TextualReason(text = reason))