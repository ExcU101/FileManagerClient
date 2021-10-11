package com.excu_fcd.filemanagerclient.mvvm.feature.worker.task

import com.excu_fcd.filemanagerclient.mvvm.data.local.LocalUriModel
import com.excu_fcd.filemanagerclient.mvvm.data.request.Request

data class WorkDependency(val request: Request<LocalUriModel>)