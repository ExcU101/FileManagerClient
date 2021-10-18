package com.excu_fcd.filemanagerclient.mvvm.feature.manager.local

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import com.excu_fcd.filemanagerclient.mvvm.data.local.LocalUriModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.cancel
import java.io.IOException

class LocalContentProvider : ContentProvider() {

    private val scope = CoroutineScope(Unconfined)

    override fun onCreate(): Boolean {
        return true
    }

    override fun shutdown() {
        scope.cancel()
    }


    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?,
    ): Cursor {
        val pColumns = projection ?: arrayOf("")
        val path = LocalUriModel(uri = uri)
        val columns = mutableListOf<String>()
        val values = mutableListOf<Any?>()

        loop@ for (column in pColumns) {
            @Suppress("DEPRECATION")
            when (column) {
                OpenableColumns.DISPLAY_NAME -> {
                    columns += column
                    values += path.getName()
                }
                MediaStore.MediaColumns.SIZE -> {
                    val size = try {
                        path.fromLength()
                    } catch (e: UnsupportedOperationException) {
                        e.printStackTrace()
                        null
                    }
                    columns += column
                    values += size
                }
                MediaStore.MediaColumns.DATA -> {
                    val file = try {
                        path.getFile()
                    } catch (e: UnsupportedOperationException) {
                        continue@loop
                    }
                    columns += column
                    values += file.absolutePath
                }
                DocumentsContract.Document.COLUMN_MIME_TYPE -> {
                    columns += column
                    values += path.getExtension()
                }
                DocumentsContract.Document.COLUMN_LAST_MODIFIED -> {
                    val lastModified = try {
                        path.getLastModifiedTime()
                    } catch (e: IOException) {
                        e.printStackTrace()
                        null
                    }
                    columns += column
                    values += lastModified
                }
            }
        }

        return MatrixCursor(columns.toTypedArray(), 1).apply {
            addRow(values)
        }
    }

    override fun openFile(uri: Uri, mode: String): ParcelFileDescriptor? {
        val path = LocalUriModel(uri)
        return ParcelFileDescriptor.open(path.getFile(), ParcelFileDescriptor.parseMode(mode))
    }

    override fun getType(uri: Uri): String {
        return LocalUriModel(uri = uri).getExtension()
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        error("No insert")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        error("No delete")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?,
    ): Int {
        error("No update")
    }

    private val Uri.providerFromPath: String
        get() {
            return path!!
        }

}