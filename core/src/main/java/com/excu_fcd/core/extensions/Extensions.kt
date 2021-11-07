package com.excu_fcd.core.extensions

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import com.excu_fcd.core.data.model.DocumentModel
import java.io.File

fun isGranted(context: Context, permission: String) =
    ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

fun DocumentFile.asDocumentModel(isDirectory: Boolean = false) = DocumentModel(this, isDirectory)

fun <T : Any> T.logIt(): T {
    Log.i("Loggable ($javaClass)", toString())
    return this
}

fun <T> MutableList<T>.items(items: List<T>) {
    addAll(items)
}

fun File.asDocumentModel(isDirectory: Boolean = false) =
    DocumentFile.fromFile(this).asDocumentModel(isDirectory = isDirectory)

fun <T> MutableList<T>.items(block: MutableList<T>.() -> Unit) {
    addAll(mutableListOf<T>().apply(block))
}

fun Uri.asDocumentFile(context: Context) = DocumentFile.fromSingleUri(context, this)

fun DocumentModel.getConcatenatedMimeType(): String {
    val mimeType = getMimeType()
    if (mimeType.isEmpty()) {
        return ""
    }

    val type = if (isDirectory()) if (mimeType == "Empty folder") "" else "Items: " else "Type: "
    return type + mimeType
}