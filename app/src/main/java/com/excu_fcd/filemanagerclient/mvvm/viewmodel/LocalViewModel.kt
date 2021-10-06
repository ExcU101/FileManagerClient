package com.excu_fcd.filemanagerclient.mvvm.viewmodel

import android.os.Build
import androidx.lifecycle.viewModelScope
import com.excu_fcd.filemanagerclient.mvvm.data.local.LocalUriModel
import com.excu_fcd.filemanagerclient.mvvm.data.request.Request
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.local.*
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.local.LocalManager.Companion.SDCARD
import com.excu_fcd.filemanagerclient.mvvm.feature.worker.result.Result
import com.excu_fcd.filemanagerclient.mvvm.viewmodel.state.LoadingState
import com.excu_fcd.filemanagerclient.mvvm.viewmodel.state.ViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class LocalViewModel @Inject constructor(
    private val manager: LocalManager,
) : BaseEventViewModel() {

    init {
        viewModelScope.launch {
            getEvent(startItem = LoadingState())
        }
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