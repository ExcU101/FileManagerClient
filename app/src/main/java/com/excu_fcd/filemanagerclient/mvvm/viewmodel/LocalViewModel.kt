package com.excu_fcd.filemanagerclient.mvvm.viewmodel

import android.os.Build
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.excu_fcd.filemanagerclient.mvvm.data.local.LocalUriModel
import com.excu_fcd.filemanagerclient.mvvm.data.request.Request
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.local.FolderEmptyState
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.local.LocalManager
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.local.LocalManager.Companion.SDCARD
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.local.state.RequirePermissionState
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.local.state.RequireSpecialPermissionState
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.local.state.SortedListState
import com.excu_fcd.filemanagerclient.mvvm.feature.worker.result.Result
import com.excu_fcd.filemanagerclient.mvvm.utils.asLocalUriModel
import com.excu_fcd.filemanagerclient.mvvm.viewmodel.state.LoadingState
import com.excu_fcd.filemanagerclient.mvvm.viewmodel.state.ViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class LocalViewModel @Inject constructor(
    private val manager: LocalManager,
    private val state: SavedStateHandle,
) : BaseEventViewModel() {

    private val _refreshFlow = MutableStateFlow(false)
    val refreshState
        get() = _refreshFlow.asStateFlow()

    private val _loadingFlow = MutableStateFlow(false)
    val loadingState
        get() = _loadingFlow.asStateFlow()

    private val _currentPathFlow = MutableStateFlow(SDCARD.asLocalUriModel())
    val currentPathFlow
        get() = _currentPathFlow.asStateFlow()

    init {
        viewModelScope.launch {
            update()
        }
    }

    fun restore() {
        state.get<String>("currentPath")
    }

    fun refresh(path: LocalUriModel = this.currentPathFlow.value) {
        viewModelScope.launch {
            _refreshFlow.emit(true)
            _flow.emit(LoadingState)
            val list = manager.getListFromPath(path = path.getFile())
            if (list.isNotEmpty()) {
                _flow.emit(SortedListState(list))
            }
        }
        _refreshFlow.tryEmit(false)
    }

    fun update() {
        viewModelScope.launch {
            getEvent(LoadingState)
        }
    }

    fun updateFromFile(file: File, startState: ViewModelState = LoadingState) {
        viewModelScope.launch {
            getEventFromFile(startItem = startState, file = file)
        }
    }

    suspend fun request(request: Request<LocalUriModel>, onResponse: (Result) -> Unit) {
        manager.sendRequest(request = request, onResponse = onResponse)
    }

    private suspend fun getEventFromFile(
        startItem: ViewModelState,
        file: File,
    ) {
        _loadingFlow.emit(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!manager.requireSpecialPermission()) {
                _loadingFlow.emit(false)
                _flow.emit(value = RequireSpecialPermissionState())
            }
        } else {
            if (!manager.checkPermissions()) {
                _loadingFlow.emit(false)
                return _flow.emit(value = RequirePermissionState())
            }
        }

        with(manager.getListFromPath(file)) {
            if (isEmpty()) {
                _loadingFlow.emit(false)
                return _flow.emit(value = FolderEmptyState(file = file))
            }
            _loadingFlow.emit(false)
            return _flow.emit(value = SortedListState(list = sortedBy { it.getName() }))
        }
    }


    override suspend fun getEvent(startItem: ViewModelState) {
        getEventFromFile(startItem = startItem, file = SDCARD)
    }

}