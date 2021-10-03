package com.excu_fcd.filemanagerclient.mvvm.feature.sort

import com.excu_fcd.filemanagerclient.mvvm.data.UriModel
import com.excu_fcd.filemanagerclient.mvvm.data.local.LocalUriModel

interface Sort<I : UriModel> {

    companion object : Sort<LocalUriModel> {

    }
}