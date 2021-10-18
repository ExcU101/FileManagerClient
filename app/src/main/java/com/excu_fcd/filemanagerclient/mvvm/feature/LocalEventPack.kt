package com.excu_fcd.filemanagerclient.mvvm.feature

import com.excu_fcd.filemanagerclient.mvvm.data.local.LocalUriModel
import com.excu_fcd.filemanagerclient.mvvm.data.request.type.OperationType
import com.excu_fcd.filemanagerclient.mvvm.feature.worker.result.Result

data class LocalEventPack(
    val type: OperationType,
    val item: LocalUriModel,
    val result: Result,
) : EventPack