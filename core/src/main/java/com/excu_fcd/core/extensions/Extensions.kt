package com.excu_fcd.core.extensions

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import com.excu_fcd.core.data.model.DocumentModel

fun isGranted(context: Context, permission: String) =
    ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

fun DocumentFile.asDocumentModel() = DocumentModel(this)

fun <T : Any> T.logIt(): T {
    Log.i("Loggable ($javaClass)", toString())
    return this
}

fun Uri.asDocumentFile(context: Context) = DocumentFile.fromSingleUri(context, this)