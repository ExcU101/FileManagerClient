package com.excu_fcd.filemanagerclient.mvvm.filemanager.model.state

import com.excu_fcd.filemanagerclient.mvvm.data.local.LocalUriModel

data class LocalDisplayStateModel(val items: Map<Boolean, List<LocalUriModel>>) : LocalStateModel
