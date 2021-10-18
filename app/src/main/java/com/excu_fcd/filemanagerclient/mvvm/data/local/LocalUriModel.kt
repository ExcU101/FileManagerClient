package com.excu_fcd.filemanagerclient.mvvm.data.local

import android.net.Uri
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.excu_fcd.filemanagerclient.mvvm.data.UriModel
import com.excu_fcd.filemanagerclient.mvvm.data.attrs.MimeType
import com.excu_fcd.filemanagerclient.mvvm.data.attrs.Size
import com.excu_fcd.filemanagerclient.mvvm.data.filesystem.FileSystem
import com.excu_fcd.filemanagerclient.mvvm.data.filesystem.LinuxFileSystem
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.local.LocalManager.Companion.SDCARD
import com.excu_fcd.filemanagerclient.mvvm.utils.asLocalUriModel
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
class LocalUriModel(
    private val uri: Uri = SDCARD.toUri(),
    private val mustBeDirectory: Boolean = false,
    private val fileSystem: FileSystem = LinuxFileSystem(),
) : UriModel(uri = uri, fileSystem = fileSystem), Size, MimeType {

    fun getFile() = uri.toFile()

    @IgnoredOnParcel
    private val _extension = if (getName().contains(".") && !isDirectory()) {
        getName().substring(getName().lastIndexOf(".") + 1)
    } else if (isDirectory()) {
        getFile().list()?.count().toString()
    } else {
        "BIN"
    }

    fun getLastModifiedTime() = getFile().lastModified()

//    fun getNamedParent(): String? =
//        if (getPath() == "Null path") null else getPath().substring(getPath().lastIndexOf("/"))

    fun getParent(): LocalUriModel? = uri.toFile().parentFile?.asLocalUriModel()

    override fun getPath(): String = if (isAbsolute()) getFile().absolutePath else super.getPath()

    @IgnoredOnParcel
    val extendedExtension: String = (if (isDirectory()) "Items: " else "Type: ") + _extension

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

    override fun isSupportable(): Boolean {
        return true
    }

    fun getSize(): Long {
        var index = 0
        var mLength = getFile().length()

        while (mLength > 1024) {
            mLength /= 1024
            index += 1
        }

        return mLength
    }

    fun getExtension() = _extension

    fun isDirectory(): Boolean = getFile().isDirectory || mustBeDirectory

    fun isImage(): Boolean {
        return !isDirectory() && getExtension() == ".jpg" || getExtension() == ".png"
    }
}



