package com.excu_fcd.core.provider.job.callback

import com.excu_fcd.core.data.model.DocumentModel
import com.excu_fcd.core.data.request.operation.Operation
import com.excu_fcd.core.data.request.operation.reason.Reason

abstract class RenameModelJobCallback : ModelJobCallback() {

    abstract fun onRenameSuccess(
        operation: Operation,
        oldModel: DocumentModel,
        destModel: DocumentModel,
        reason: Reason
    )

    abstract fun onRenameFailure(
        operation: Operation,
        oldModel: DocumentModel,
        destModel: DocumentModel,
        reason: Reason
    )

}