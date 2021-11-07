package com.excu_fcd.core.provider.job.callback

import com.excu_fcd.core.data.request.operation.Operation
import com.excu_fcd.core.data.request.operation.reason.Reason

abstract class OperationJobCallback : JobCallback() {

    abstract fun onGranted(operation: Operation, reason: Reason = Reason.empty())

    abstract fun onDenied(operation: Operation, reason: Reason)

}