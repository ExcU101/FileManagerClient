package com.excu_fcd.filemanagerclient.mvvm.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _flow = MutableStateFlow("File")
    val flow
        get() = _flow.asStateFlow()

    fun change(value: String) {
        viewModelScope.launch {
            _flow.emit(value)
        }
    }
}