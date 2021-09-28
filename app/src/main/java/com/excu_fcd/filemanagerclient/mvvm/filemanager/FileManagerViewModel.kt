package com.excu_fcd.filemanagerclient.mvvm.filemanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.local.LocalManager
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.local.LocalManager.Companion.SDCARD
import com.excu_fcd.filemanagerclient.mvvm.filemanager.model.state.LocalDisplayStateModel
import com.excu_fcd.filemanagerclient.mvvm.filemanager.model.state.LocalLoadingStateModel
import com.excu_fcd.filemanagerclient.mvvm.filemanager.model.state.LocalRequirePermissionsStateModel
import com.excu_fcd.filemanagerclient.mvvm.filemanager.model.state.LocalStateModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FileManagerViewModel @Inject constructor(private val localManager: LocalManager) :
    ViewModel() {

    private val _files = MutableStateFlow<LocalStateModel>(LocalLoadingStateModel)
    val files: StateFlow<LocalStateModel>
        get() = _files.asStateFlow()

    init {
        reload()
    }

    fun reload() {
        viewModelScope.launch {
            _files.emit(LocalLoadingStateModel)
            if (!localManager.checkPermissions()) {
                return@launch _files.emit(LocalRequirePermissionsStateModel)
            }
            val files = localManager.getListFromPath(SDCARD)
            _files.emit(LocalDisplayStateModel(files))
        }

    }

}