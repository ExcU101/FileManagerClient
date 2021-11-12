package com.excu_fcd.core.data.model

import android.content.Context
import android.net.Uri
import android.os.Parcelable
import androidx.core.net.toFile
import androidx.documentfile.provider.DocumentFile
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class DocumentModelUri(val uri: Uri) : Parcelable {

    fun getScheme() = uri.scheme

    fun deleteRecursivelyByFile(file: File): Boolean {
        if (file.isDirectory) {
            file.listFiles()?.let {
                for (f in it) {
                    deleteRecursivelyByFile(f)
                }
            }
        }
        return file.deleteRecursively()
    }

    fun deleteRecursivelyByDocument(file: DocumentFile): Boolean {
        if (file.isDirectory) {
            for (it in file.listFiles()) {
                deleteRecursivelyByDocument(it)
            }
        }
        return file.delete()
    }

    fun isAndroidContent() = getScheme() == "content"

    fun isClientUri() = getScheme() == "client"

    fun toFile() = uri.toFile()

    fun toSingleDocument(context: Context) = DocumentFile.fromSingleUri(context, uri)

    fun toTreeDocument(context: Context) = DocumentFile.fromTreeUri(context, uri)

}