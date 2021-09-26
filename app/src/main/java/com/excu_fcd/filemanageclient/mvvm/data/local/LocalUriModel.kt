package com.excu_fcd.filemanageclient.mvvm.data.local

import android.net.Uri
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.excu_fcd.filemanageclient.mvvm.data.UriModel
import com.excu_fcd.filemanageclient.mvvm.feature.manager.local.LocalManager.Companion.SDCARD
import kotlinx.parcelize.Parcelize

@Parcelize
class LocalUriModel(
    private val uri: Uri = SDCARD.toUri(),
    private val mustBeDirectory: Boolean = false,
) : UriModel(uri = uri) {

    fun getFile() = uri.toFile()

    fun isDirectory(): Boolean {
        return getFile().isDirectory || mustBeDirectory
    }

}