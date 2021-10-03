package com.excu_fcd.filemanagerclient.mvvm.data.local

import android.net.Uri
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.excu_fcd.filemanagerclient.mvvm.data.Size
import com.excu_fcd.filemanagerclient.mvvm.data.UriModel
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.local.LocalManager.Companion.SDCARD
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
class LocalUriModel(
    private val uri: Uri = SDCARD.toUri(),
    private val mustBeDirectory: Boolean = false,
) : UriModel(uri = uri), Size {

    fun getFile() = uri.toFile()

    @IgnoredOnParcel
    public val extendedExtension: String = (if (isDirectory()) "Items: " else "Type: ") + getExtension()

    @IgnoredOnParcel
    private val _extension = if (getName().contains(".") && !isDirectory()) {
        getName().substring(getName().lastIndexOf("."))
    } else if (isDirectory()) {
        getFile().list()?.count().toString()
    } else {
        "BIN"
    }

    override fun fromLength(): String {
        var index = 0
        var mLength = getFile().length()

        while (mLength > 1024) {
            mLength /= 1024
            index += 1
        }

        return when (index) {
            1 -> "$mLength KB"
            2 -> "$mLength MB"
            3 -> "$mLength GB"
            4 -> "$mLength TB"
            else -> "$mLength B"
        }
    }

    fun getExtension() = _extension

    fun isDirectory(): Boolean = getFile().isDirectory || mustBeDirectory

    fun isImage(): Boolean {
        return !isDirectory() && getExtension() == ".jpg" || getExtension() == ".png"
    }
}



