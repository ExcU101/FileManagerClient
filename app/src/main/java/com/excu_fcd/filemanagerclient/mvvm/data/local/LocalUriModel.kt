package com.excu_fcd.filemanagerclient.mvvm.data.local

import android.net.Uri
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.excu_fcd.filemanagerclient.mvvm.data.UriModel
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.local.LocalManager.Companion.SDCARD
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
class LocalUriModel(
    private val uri: Uri = SDCARD.toUri(),
    private val mustBeDirectory: Boolean = false,
) : UriModel(uri = uri) {

    fun getFile() = uri.toFile()

    @IgnoredOnParcel
    private val _extension = if (getName().contains(".") && !isDirectory()) {
        getName().substring(getName().lastIndexOf("."))
    } else if (isDirectory()) {
        getFile().list()?.count().toString()
    } else {
        "BIN"
    }

    fun getExtension() = _extension

    fun isDirectory(): Boolean = getFile().isDirectory || mustBeDirectory

    fun isImage(): Boolean {
        return !isDirectory() && getExtension() == ".jpg" || getExtension() == ".png"
    }

}