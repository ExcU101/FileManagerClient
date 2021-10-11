package com.excu_fcd.filemanagerclient.mvvm.feature.manager.local

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.excu_fcd.filemanagerclient.mvvm.data.local.LocalUriModel

class LocalContentProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?,
    ): Cursor? {
        return null
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
}