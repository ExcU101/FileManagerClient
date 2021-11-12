package com.excu_fcd.filemanagerclient.mvvm.viewmodel

import android.os.Environment
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.excu_fcd.core.data.model.DocumentModel
import com.excu_fcd.core.data.request.Request
import com.excu_fcd.core.provider.DocumentManager
import com.excu_fcd.core.provider.DocumentManager.Companion.CURRENT_PATH_STATE
import com.excu_fcd.core.provider.job.callback.ModelJobCallback
import com.excu_fcd.core.provider.job.callback.OperationJobCallback
import com.excu_fcd.filemanagerclient.mvvm.utils.getStateFlow
import com.excu_fcd.filemanagerclient.mvvm.viewmodel.state.ListState
import com.excu_fcd.filemanagerclient.mvvm.viewmodel.state.ViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DocumentViewModel @Inject constructor(
    private val manager: DocumentManager,
    private val state: SavedStateHandle,
) : ViewModel() {

    private val _currentPath =
        state.getStateFlow(
            viewModelScope,
            initialValue = DocumentModel(DocumentFile.fromFile(Environment.getExternalStorageDirectory())),
            key = CURRENT_PATH_STATE
        )
    val currentPathFlow
        get() = _currentPath.asStateFlow()

    private val _state = MutableStateFlow(ViewModelState.empty())
    val stateFlow
        get() = _state.asStateFlow()

    private val _refresh = MutableStateFlow(false)
    val refreshFlow
        get() = _refresh.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loadingFlow
        get() = _loading.asStateFlow()

    init {
        viewModelScope.launch {
            loadState(_currentPath.value)
        }
    }

    fun refreshState() {
        viewModelScope.launch {
            _refresh.emit(value = true)
            delay(timeMillis = 500L)
            launchState(path = _currentPath.value)
            _refresh.emit(value = false)
        }
    }

    fun loadState(path: DocumentModel = _currentPath.value) {
        viewModelScope.launch {
            _loading.emit(value = true)
            delay(timeMillis = 500L)
            launchState(path = path)
            _loading.emit(value = false)
        }
    }

    fun updateState(path: DocumentModel = _currentPath.value) {
        viewModelScope.launch {
            launchState(path)
        }
    }

    suspend fun request(
        request: Request,
        operationCallback: OperationJobCallback? = null,
        operationItemCallback: ModelJobCallback? = null,
    ) {
        manager.pullRequest(request, operationCallback, operationItemCallback)
    }

    private suspend fun launchState(path: DocumentModel = _currentPath.value) {
        _currentPath.emit(path)

        try {
            val list = manager.getListFromPath(path)
            _state.emit(ListState(list.sorted()))
        } catch (e: IllegalArgumentException) {
            e.stackTrace
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}