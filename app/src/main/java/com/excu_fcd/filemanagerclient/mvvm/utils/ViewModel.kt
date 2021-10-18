package com.excu_fcd.filemanagerclient.mvvm.utils

import com.excu_fcd.filemanagerclient.mvvm.data.local.LocalUriModel
import com.excu_fcd.filemanagerclient.mvvm.data.request.type.CreateOperationType
import com.excu_fcd.filemanagerclient.mvvm.data.request.type.DeleteOperationType
import com.excu_fcd.filemanagerclient.mvvm.feature.LocalEventPack
import com.excu_fcd.filemanagerclient.mvvm.viewmodel.LocalViewModel

suspend fun LocalViewModel.requestCreate(
    item: LocalUriModel,
    onItemResponse: (LocalEventPack) -> Unit,
) {
    request(request {
        requestId(item.getId())
        requestName("Create ${item.getName()}")
        requestOperations { item(item, CreateOperationType) }
    }, onItemResponse = onItemResponse)
}

suspend fun LocalViewModel.requestDelete(
    item: LocalUriModel,
    onResponse: (LocalEventPack) -> Unit,
) {
    request(
        request {
            requestId(item.getId())
            requestName("Delete ${item.getName()}")
            requestOperations { item(element = item, type = DeleteOperationType) }
        },
        onItemResponse = onResponse
    )
}

suspend fun LocalViewModel.requestCreate(
    data: List<LocalUriModel>,
    onResponse: (LocalEventPack) -> Unit,
) {
    request(
        request {
            requestId(data.size)
            requestName("Delete list $data")
            requestOperations { items(data, CreateOperationType) }
        },
        onItemResponse = onResponse
    )
}

suspend fun LocalViewModel.requestDelete(
    data: List<LocalUriModel>,
    onResponse: (LocalEventPack) -> Unit,
) {
    request(
        request {
            requestId(data.size)
            requestName("Delete list $data")
            requestOperations { items(data, DeleteOperationType) }
        },
        onItemResponse = onResponse
    )
}