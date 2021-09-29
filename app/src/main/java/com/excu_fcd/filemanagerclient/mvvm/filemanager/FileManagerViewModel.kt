package com.excu_fcd.filemanagerclient.mvvm.filemanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.local.LocalManager
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.local.LocalManager.Companion.SDCARD
import com.excu_fcd.filemanagerclient.mvvm.filemanager.model.state.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FileManagerViewModel @Inject constructor(private val localManager: LocalManager) :
    ViewModel() {

    private val flow = MutableStateFlow<LocalStateModel>(LocalLoadingStateModel)
    val state: StateFlow<LocalStateModel>
        get() = flow.asStateFlow()

    init {
        reload()
    }

    fun reloadFromFile(startState: LocalStateModel = LocalLoadingStateModel, file: File = SDCARD) {
        viewModelScope.launch {
            flow.emit(startState)
            if (!localManager.checkPermissions()) {
                return@launch flow.emit(LocalRequirePermissionsStateModel)
            }
            if (!file.exists()) {
                return@launch flow.emit(LocalNotExistStateModel(file = file))
            }
            val files = localManager.getListFromPath(file)
            if (files.isEmpty()) {
                return@launch flow.emit(LocalEmptyDirStateModel(file = file))
            }

            flow.emit(LocalDisplayStateModel(files.sortedBy { it.getName() }
                .groupBy { it.isDirectory() }))
        }
    }

    fun reload() {
        reloadFromFile()
    }

}