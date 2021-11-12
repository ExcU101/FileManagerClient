package com.excu_fcd.core.provider

import android.database.Cursor
import android.database.MatrixCursor
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.provider.DocumentsContract.Document.*
import android.provider.DocumentsContract.Root
import android.provider.DocumentsProvider
import java.io.File
import java.io.FileNotFoundException


class StorageProvider : DocumentsProvider() {

    private val rootProjection = arrayOf(
        Root.COLUMN_ROOT_ID,
        Root.COLUMN_MIME_TYPES,
        Root.COLUMN_FLAGS,
        Root.COLUMN_ICON,
        Root.COLUMN_TITLE,
        Root.COLUMN_SUMMARY,
        Root.COLUMN_DOCUMENT_ID,
        Root.COLUMN_AVAILABLE_BYTES
    )

    private val documentProjection = arrayOf(
        COLUMN_DOCUMENT_ID,
        COLUMN_MIME_TYPE,
        COLUMN_DISPLAY_NAME,
        COLUMN_LAST_MODIFIED,
        COLUMN_FLAGS,
        COLUMN_SIZE
    )

    override fun onCreate(): Boolean {
        return true
    }

    override fun queryRoots(projection: Array<String>?): Cursor {
        val result = MatrixCursor(resolveRootProjection(projection = projection))
        val row = result.newRow()

        try {
            row.add(Root.COLUMN_ROOT_ID)
            row.add(Root.COLUMN_SUMMARY)
            row.add(
                Root.COLUMN_FLAGS,
                Root.FLAG_SUPPORTS_CREATE or
                        Root.FLAG_SUPPORTS_RECENTS or
                        Root.FLAG_SUPPORTS_SEARCH
            )
            row.add(Root.COLUMN_TITLE)
            row.add(Root.COLUMN_DOCUMENT_ID)
            row.add(Root.COLUMN_MIME_TYPES)
            row.add(Root.COLUMN_AVAILABLE_BYTES)
            row.add(Root.COLUMN_ICON)
        } catch (exception: FileNotFoundException) {
            exception.stackTraceToString()
        }
        return result
    }

    override fun queryDocument(documentId: String?, projection: Array<String>?): Cursor {
        val result = MatrixCursor(resolveRootProjection(projection = projection))
        val row = result.newRow()

        row.add(COLUMN_DOCUMENT_ID)
        row.add(COLUMN_DISPLAY_NAME)
        row.add(COLUMN_SIZE)
        row.add(COLUMN_MIME_TYPE)
        row.add(COLUMN_LAST_MODIFIED)
        row.add(COLUMN_FLAGS)
        row.add(COLUMN_ICON)

        return result
    }

    override fun queryChildDocuments(
        parentDocumentId: String?,
        projection: Array<String>?,
        sortOrder: String?
    ): Cursor {
        val result = MatrixCursor(resolveRootProjection(projection = projection))

        return result
    }

    override fun openDocument(
        documentId: String?,
        mode: String?,
        signal: CancellationSignal?
    ): ParcelFileDescriptor {

        val file = File("")

        return ParcelFileDescriptor.open(file, ParcelFileDescriptor.parseMode(mode))
    }

    private fun resolveRootProjection(projection: Array<String>?): Array<String> {
        return projection ?: rootProjection
    }
}