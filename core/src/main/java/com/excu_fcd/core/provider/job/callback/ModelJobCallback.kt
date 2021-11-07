package com.excu_fcd.core.provider.job.callback

import com.excu_fcd.core.data.model.DocumentModel
import com.excu_fcd.core.data.request.operation.Operation
import com.excu_fcd.core.data.request.operation.reason.Reason

abstract class ModelJobCallback : JobCallback() {

    abstract fun onFailure(operation: Operation, item: DocumentModel, reason: Reason)

    abstract fun onSuccess(operation: Operation, item: DocumentModel, reason: Reason)

}