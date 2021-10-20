package com.excu_fcd.core.data.model

import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import androidx.documentfile.provider.DocumentFile

class UriModel(
    private val context: Context,
    private val uri: Uri = Uri.fromParts("", "", null),
    private val isMustBeDirectory: Boolean = false,
) {

    fun getScheme(): String? = uri.scheme

    fun getPath(): String = uri.path ?: "Null path"

    fun getName(): String = if (hasSeparator()) {
        executeName()
    } else {
        "Empty name"
    }

    fun getFile(): Any? {
        return when (getScheme()) {
            "file" -> {
                uri.toFile()
            }
            "ftp" -> {
                
            }
            "content" -> {
                DocumentFile.fromSingleUri(context, uri)
            }
            else -> {
                null
            }
        }
    }

    private fun hasSeparator(): Boolean = getPath().contains("/")

    private fun executeName(): String = getPath().substring(getPath().lastIndexOf("/") + 1)
}

fun UriModel.isFileScheme(): Boolean = getScheme() == "file"
fun UriModel.isFtpFileScheme(): Boolean = getScheme() == "ftp"
fun UriModel.isDocumentFileScheme(): Boolean = getScheme() == "content"