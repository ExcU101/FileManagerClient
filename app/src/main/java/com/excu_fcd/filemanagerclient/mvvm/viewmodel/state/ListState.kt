package com.excu_fcd.filemanagerclient.mvvm.viewmodel.state

import com.excu_fcd.core.data.model.DocumentModel

data class ListState(val list: List<DocumentModel>) : ViewModelState {
}