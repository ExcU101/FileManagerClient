package com.excu_fcd.filemanagerclient.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.excu_fcd.core.provider.DocumentManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val manager: DocumentManager
) : ViewModel() {

    private val _taskFlow = MutableStateFlow<Any>(listOf<Any>())
    val tasksFlow = _taskFlow.asStateFlow()

    init {
        viewModelScope.launch {
            update()
        }
    }

    suspend fun update(names: List<String> = listOf("Delete Download")) {
        manager.getTasks(names).value?.let { infos ->
            _taskFlow.emit(infos.map { it.state })
        }
    }

}