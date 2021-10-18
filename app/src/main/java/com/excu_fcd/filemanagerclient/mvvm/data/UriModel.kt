package com.excu_fcd.filemanagerclient.mvvm.data

import android.net.Uri
import android.os.Environment
import android.os.Parcelable
import androidx.core.net.toUri
import com.excu_fcd.filemanagerclient.mvvm.data.filesystem.FileSystem
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

@Suppress("DEPRECATION")
@Parcelize
open class UriModel(
    private val uri: Uri = Environment.getExternalStorageDirectory().toUri(),
    private val fileSystem: FileSystem,
) : Parcelable, Idable, Nameable {

    @IgnoredOnParcel
    private val _path = uri.path ?: "Null path"

    @IgnoredOnParcel
    private val _name = if (_path.contains("/")) {
        _path.substring(_path.lastIndexOf("/") + 1)
    } else {
        _path
    }

    fun isAbsolute() = uri.isAbsolute

    open fun getPath(): String = _path

    override fun getId(): Int {
        return Random.nextInt()
    }


    override fun getName(): String {
        return _name
    }

}