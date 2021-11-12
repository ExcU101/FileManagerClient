package com.excu_fcd.core.extensions

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import com.excu_fcd.core.data.model.DocumentModel
import java.io.File

fun isGranted(context: Context, permission: String) =
    ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

fun DocumentFile.asDocumentModel(isDirectory: Boolean = false) = DocumentModel(this, isDirectory)

fun <T : Any> T.logIt(): T {
    Log.i("Loggable ($javaClass)", toString())
    return this
}

fun isAndroidR() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R

fun <T> MutableList<T>.items(items: List<T>) {
    addAll(items)
}

fun <T> MutableList<T>.logEachItem() {
    forEach {
        it?.logIt()
    }
}

fun Fragment.grantUri(uri: Uri, flags: Int) {
    requireContext().grantUri(uri, flags)
}

fun Context.grantUri(uri: Uri, flags: Int) {
    contentResolver.takePersistableUriPermission(uri, flags)
//    grantUriPermission(packageName, uri, flags)
}

fun File.asDocumentModel(isDirectory: Boolean = false) =
    DocumentFile.fromFile(this).asDocumentModel(isDirectory = isDirectory)

fun <T> MutableList<T>.items(block: MutableList<T>.() -> Unit) {
    addAll(mutableListOf<T>().apply(block))
}

fun Uri.asDocumentModel(context: Context) =
    DocumentFile.fromSingleUri(context, this)?.asDocumentModel()

fun Uri.asDocumentTreeModel(context: Context) =
    DocumentFile.fromTreeUri(context, this)?.asDocumentModel()

fun Uri.asDocumentFile(context: Context) = DocumentFile.fromSingleUri(context, this)

fun Collection<DocumentModel>.logEachName(): Collection<DocumentModel> {
    forEach {
        it.getName().logIt()
    }
    return this
}