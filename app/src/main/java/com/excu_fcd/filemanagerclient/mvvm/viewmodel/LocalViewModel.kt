package com.excu_fcd.filemanagerclient.mvvm.viewmodel

import android.os.Build
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
) : BaseEventViewModel() {

    private val _refreshFlow = MutableStateFlow(false)
    val refreshState
        get() = _refreshFlow.asStateFlow()

    init {
        viewModelScope.launch {
            update()
        }
    }

    fun refresh(path: LocalUriModel) {
        viewModelScope.launch {
            _refreshFlow.emit(true)
            _flow.emit(SortedListState(manager.getListFromPath(path = path.getFile())))
        }
        _refreshFlow.tryEmit(false)
    }

    fun update() {
        viewModelScope.launch {
            getEvent(LoadingState())
        }
    }

    suspend fun request(request: Request<LocalUriModel>, onResponse: (Result) -> Unit) {
        manager.sendRequest(request = request, onResponse = onResponse)
    }

    suspend fun getEventFromFile(
        startItem: ViewModelState,
        file: File,
    ) {
        _flow.emit(value = startItem)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!manager.requireSpecialPermission()) {
                _flow.emit(value = RequireSpecialPermissionState())
            }
        } else {
            if (!manager.checkPermissions()) {
                return _flow.emit(value = RequirePermissionState())
            }
        }

        val list = manager.getListFromPath(file)

        if (list.isEmpty()) {
            return _flow.emit(value = FolderEmptyState(file = file))
        }
        return _flow.emit(value = SortedListState(list = list.sortedBy { it.getName() }))
    }

    override suspend fun getEvent(startItem: ViewModelState) {
        getEventFromFile(startItem = startItem, file = SDCARD)
    }

}