package com.excu_fcd.core.data.model

import android.os.Environment
import android.os.Parcelable
import androidx.documentfile.provider.DocumentFile
import com.excu_fcd.core.extensions.asDocumentModel
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
class DocumentModel(
    private val file: @RawValue DocumentFile = DocumentFile.fromFile(Environment.getExternalStoragePublicDirectory(
        "")),
    private val isMustBeDirectory: Boolean = false,
) : Parcelable {

    fun exists() = file.exists()

    private fun delete(file: DocumentFile): Boolean {
        if (isDirectory()) {
            for (document in file.listFiles()) {
                delete(document)
            }
        }
        return file.delete()
    }

    /*
     * Create file or directory inside this model (if is directroy)
     */
    fun createInside(isDir: Boolean = false): Boolean {
        return if (isDirectory()) {
            (if (isDir) file.createDirectory(getName()) else file.createFile(getMimeType(),
                getName()))?.exists() ?: false
        } else {
            false
        }
    }

    /**
     * Deletes this file.
     */
    fun delete(): Boolean {
        return delete(file)
    }

    @IgnoredOnParcel
    private val uri = file.uri

    fun getScheme(): String? = uri.scheme

    fun getPath(): String = uri.path ?: "Null path"

    fun getName(): String = if (hasSeparator()) {
        executeName()
    } else {
        "Empty name"
    }

    fun getParent() = file.parentFile

    fun getMimeType(): String {
        return if (getName().contains(".") && getName().startsWith(".")) {
            executeMimeType()
        } else {
            file.listFiles().count().toString()
        }
    }

    fun list(): List<DocumentModel>? =
        if (!isDirectory()) null else file.listFiles().map { it.asDocumentModel() }


    fun isDirectory(): Boolean = file.isDirectory || isMustBeDirectory

    private fun hasSeparator(): Boolean = getPath().contains("/")

    private fun hasNotSeparator(): Boolean = !hasSeparator()

    private fun executeName(): String = getPath().substring(getPath().lastIndexOf("/") + 1)

    private fun executeMimeType() = getName().substring(getName().lastIndexOf(".") + 1)

}