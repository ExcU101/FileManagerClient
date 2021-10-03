package com.excu_fcd.filemanagerclient.mvvm.feature.manager.local

import com.excu_fcd.filemanagerclient.mvvm.data.local.LocalUriModel
import com.excu_fcd.filemanagerclient.mvvm.feature.sort.Sort
import com.excu_fcd.filemanagerclient.mvvm.viewmodel.state.ViewModelState

data class SortedListState(val list: List<LocalUriModel>) :
    ViewModelState