package com.excu_fcd.core.provider

import com.excu_fcd.core.data.request.Request
import com.excu_fcd.core.provider.job.callback.ModelJobCallback
import com.excu_fcd.core.provider.job.callback.OperationJobCallback

interface RequestedManager {

    suspend fun pullRequest(
        request: Request, operationCallback: OperationJobCallback? = null,
        itemOperationCallback: ModelJobCallback? = null
    )

}