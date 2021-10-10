package com.excu_fcd.filemanagerclient.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.excu_fcd.filemanagerclient.mvvm.feature.EventHandler
import com.excu_fcd.filemanagerclient.mvvm.viewmodel.state.ViewModelState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseEventViewModel : ViewModel(), EventHandler<ViewModelState> {

    protected val _flow = MutableStateFlow(ViewModelState.empty())
    val dataState = _flow.asStateFlow()

}