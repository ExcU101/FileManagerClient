package com.excu_fcd.core.provider

import com.excu_fcd.core.data.request.Request
import com.excu_fcd.core.data.request.operation.Operation
import com.excu_fcd.core.provider.job.Job

interface RequestedManager {

    suspend fun pullRequest(request: Request, operationCallback: Job.OperationCallback? = null, itemOperationCallback: Job.ItemOperationCallback? = null)

}