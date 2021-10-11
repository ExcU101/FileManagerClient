package com.excu_fcd.filemanagerclient.mvvm.viewmodel.state

import com.excu_fcd.filemanagerclient.mvvm.data.local.LocalUriModel

data class ScreenState(
    val isLoading: Boolean,
    val isRefreshing: Boolean,
    val currentPath: LocalUriModel,
    val listState: ViewModelState,
)