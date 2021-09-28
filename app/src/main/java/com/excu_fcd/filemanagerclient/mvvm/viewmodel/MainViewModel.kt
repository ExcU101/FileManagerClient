package com.excu_fcd.filemanagerclient.mvvm.viewmodel

import androidx.lifecycle.viewModelScope
import com.excu_fcd.filemanagerclient.mvvm.data.local.LocalUriModel
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.local.LocalManager
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.local.LocalManager.Companion.SDCARD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val local: LocalManager) :
    BaseListViewModel<LocalUriModel>() {

    fun logAll() {
        viewModelScope.launch {
            _flow.collect {
                it.forEach { file ->
                    println(file.getName())
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            _flow.emit(
                local.getListFromPath(SDCARD).sortedBy {
                    it.getName()
                } as MutableList<LocalUriModel>
            )
        }
    }

}