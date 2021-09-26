package com.excu_fcd.filemanageclient.mvvm.viewmodel

import androidx.lifecycle.viewModelScope
import com.excu_fcd.filemanageclient.mvvm.data.local.LocalUriModel
import com.excu_fcd.filemanageclient.mvvm.feature.manager.local.LocalManager
import com.excu_fcd.filemanageclient.mvvm.feature.manager.local.LocalManager.Companion.SDCARD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val local: LocalManager) :
    BaseListViewModel<LocalUriModel>() {

    fun logAll() {
        viewModelScope.launch {
            flow.collect {
                it.forEach { file ->
                    println(file.getName())
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            flow.emit(
                local.getListFromPath(SDCARD).sortedBy {
                    it.getName()
                } as MutableList<LocalUriModel>
            )
        }
    }

}