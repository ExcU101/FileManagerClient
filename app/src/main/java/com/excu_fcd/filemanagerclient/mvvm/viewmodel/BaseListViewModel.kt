package com.excu_fcd.filemanagerclient.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseListViewModel<I> : ViewModel() {
    protected val flow = MutableStateFlow(mutableListOf<I>())

    fun getData(): StateFlow<List<I>> = flow.asStateFlow()

}