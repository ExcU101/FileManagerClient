package com.excu_fcd.filemanagerclient.mvvm.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.excu_fcd.core.data.model.DocumentModel
import com.excu_fcd.core.data.request.Request
import com.excu_fcd.core.provider.DocumentManager
import com.excu_fcd.core.provider.DocumentManager.Companion.CURRENT_PATH_STATE
import com.excu_fcd.core.provider.job.Job
import com.excu_fcd.filemanagerclient.mvvm.utils.getStateFlow
import com.excu_fcd.filemanagerclient.mvvm.utils.logIt
import com.excu_fcd.filemanagerclient.mvvm.viewmodel.state.ListState
import com.excu_fcd.filemanagerclient.mvvm.viewmodel.state.ViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
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
        state.getStateFlow(viewModelScope, initialValue = DocumentModel(), key = CURRENT_PATH_STATE)
    val currentPathFlow
        get() = _currentPath.asStateFlow()


    private val _state = MutableStateFlow(ViewModelState.empty())
    val stateFlow
        get() = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _currentPath.apply {
                launchState(value)
            }
        }
    }

    fun updateState(path: DocumentModel = _currentPath.value) {
        viewModelScope.launch {
            launchState(path)
        }
    }

    suspend fun request(
        request: Request,
        operationCallback: Job.OperationCallback? = null,
        operationItemCallback: Job.ItemOperationCallback? = null,
    ) {
        manager.pullRequest(request, operationCallback, operationItemCallback)
    }

    private suspend fun launchState(path: DocumentModel = _currentPath.value) {
        _currentPath.emit(path)
        val list = manager.getListFromPath(path)
        list.forEach {
            it.getName().logIt()
        }
        _state.emit(ListState(list.sortedBy { it.getName() }))
    }

}