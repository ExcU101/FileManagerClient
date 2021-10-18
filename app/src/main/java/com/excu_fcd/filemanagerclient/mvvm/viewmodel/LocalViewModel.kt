package com.excu_fcd.filemanagerclient.mvvm.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.excu_fcd.filemanagerclient.mvvm.data.local.LocalUriModel
import com.excu_fcd.filemanagerclient.mvvm.data.request.Request
import com.excu_fcd.filemanagerclient.mvvm.feature.LocalEventPack
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.local.FolderEmptyState
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.local.LocalManager
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.local.LocalManager.Companion.SDCARD
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.local.state.SortedListState
import com.excu_fcd.filemanagerclient.mvvm.utils.asLocalUriModel
import com.excu_fcd.filemanagerclient.mvvm.utils.getStateFlow
import com.excu_fcd.filemanagerclient.mvvm.viewmodel.state.LoadingState
import com.excu_fcd.filemanagerclient.mvvm.viewmodel.state.ViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocalViewModel @Inject constructor(
    private val manager: LocalManager,
    private val state: SavedStateHandle,
) : ViewModel() {

    private val _refreshFlow = MutableStateFlow(false)
    val refreshState
        get() = _refreshFlow.asStateFlow()

    private val _loadingFlow = MutableStateFlow(false)
    val loadingState
        get() = _loadingFlow.asStateFlow()

    private val _currentPathFlow =
        state.getStateFlow(
            scope = viewModelScope,
            key = "currentPath",
            initialValue = SDCARD.asLocalUriModel()
        )

    val currentPathFlow
        get() = _currentPathFlow.asStateFlow()

    private val _dataState: MutableStateFlow<ViewModelState> =
        MutableStateFlow(LoadingState)
    val dataState
        get() = _dataState.asStateFlow()

    init {
        viewModelScope.launch {
            updateState()
        }
    }

    private fun pushPath(model: LocalUriModel = _currentPathFlow.value) {
        state.set("currentPath", model)
    }

    fun updateState(
        model: LocalUriModel = _currentPathFlow.value,
    ) {
        viewModelScope.launch {
            load()
            pushPath(model)
            _dataState.value = getState(model = model)
            stop()
        }
    }

    fun refreshState(
        model: LocalUriModel = _currentPathFlow.value,
    ) {
        viewModelScope.launch {
            refresh()
            delay(1000L)
            _dataState.value = getState(model = model)
            stop()
        }
    }

    suspend fun request(request: Request<LocalUriModel>, onItemResponse: (LocalEventPack) -> Unit) {
        manager.sendRequest(request = request, onResponse = onItemResponse)
    }

    private suspend fun getState(
        model: LocalUriModel,
    ): ViewModelState {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            if (manager.requireSpecialPermission()) return RequireSpecialPermissionState()
//        } else {
//            if (manager.requirePermissions()) return RequirePermissionState()
//        }

        val list = manager.getListFromPath(model = model)

        if (list.isEmpty()) {
            return FolderEmptyState(model)
        }

        return SortedListState(list = list)
    }

    private suspend fun load() {
        _refreshFlow.emit(false)
        _loadingFlow.emit(true)
    }

    private suspend fun refresh() {
        _loadingFlow.emit(false)
        _refreshFlow.emit(true)
    }

    private suspend fun stop() {
        _loadingFlow.emit(false)
        _refreshFlow.emit(false)
    }

    fun cancel() {
        viewModelScope.cancel()
    }

}