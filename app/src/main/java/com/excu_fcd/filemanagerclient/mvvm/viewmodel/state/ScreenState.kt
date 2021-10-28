package com.excu_fcd.filemanagerclient.mvvm.viewmodel.state

import com.excu_fcd.core.data.model.DocumentModel

data class ScreenState(
    val isLoading: Boolean,
    val isRefreshing: Boolean,
    val currentPath: DocumentModel,
    val listState: ViewModelState,
)