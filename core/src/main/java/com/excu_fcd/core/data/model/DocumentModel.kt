package com.excu_fcd.core.data.model

import android.os.Parcelable
import androidx.documentfile.provider.DocumentFile
import com.excu_fcd.core.extensions.asDocumentModel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith
import javax.inject.Inject

@Parcelize
class DocumentModel @Inject constructor(
    private val file: @WriteWith<ParceledFile> DocumentFile,
    private val isMustBeDirectory: Boolean = false,
) : Parcelable, Comparable<DocumentModel> {

    override fun compareTo(other: DocumentModel): Int {
        return getName().length - other.getName().length
    }

    fun getSize(): String {
        var length = file.length()
        var index = 0

        if (length <= 8) {
            return "$length Bit"
        }

        while (length > 1024) {
            length /= 1024
            index++
        }

        return "$length ${
            when (index) {
                8 -> "YB"
                7 -> "ZB"
                6 -> "EB"
                5 -> "PB"
                4 -> "TB"
                3 -> "GB"
                2 -> "MB"
                1 -> "KB"
                else -> "B"
            }
        }"
    }

    fun toUri() = file.uri

    fun exists(): Boolean = file.exists()

    private fun delete(file: DocumentFile): Boolean {
        if (isDirectory()) {
            for (document in file.listFiles()) {
                delete(document)
            }
        }
        return file.delete()
    }

    fun rename(model: DocumentModel): Boolean = file.renameTo(model.getName())

    /**
     * Create file or directory inside this model (if is directory)
     */
    fun createInside(isDir: Boolean = false, model: DocumentModel): Boolean {
        return if (isDirectory()) {
            val newModel = if (isDir) file.createDirectory(model.getName()) else file.createFile(
                model.getMimeType(),
                model.getName()
            )

            newModel != null
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

    private val uri = file.uri

    fun getScheme(): String? = uri.scheme

    fun getPath(): String = uri.path ?: "Null path"

    fun getName(): String = if (hasSeparator()) {
        executeName()
    } else {
        "Empty name"
    }

    fun getPathParent() = getPath().substring(0, getPath().lastIndexOf("/"))

    fun getParentModel() = getParent()?.asDocumentModel()

    fun getParent() = file.parentFile

    fun getMimeType(): String {
        return if (getName().contains(".") && !getName().startsWith(".")) {
            val mimeType = executeMimeType()
            if (mimeType.length > 8) {
                ""
            } else {
                mimeType
            }
        } else if (isDirectory()) {

            /** Cause {@link DocumentFile} can be casted to {@link SingleDocumentFile}
             * which {@throws UnsupportedOperationException}
             * when you trying call {@link #listFiles()} method
             */
            try {
                val count = file.listFiles().count()
                if (count <= 0) {
                    "Empty folder"
                } else {
                    count.toString()
                }
            } catch (e: UnsupportedOperationException) {
                "Empty folder"
            }
        } else {
            ""
        }
    }

    fun supports() = false

    fun list(): List<DocumentModel>? =
        if (!isDirectory()) null else file.listFiles().map { it.asDocumentModel() }


    fun isDirectory(): Boolean = file.isDirectory || isMustBeDirectory

    fun isNotDirectory(): Boolean = !isDirectory()

    fun notExists(): Boolean = !exists()

    private fun hasSeparator(): Boolean = getPath().contains("/")

    private fun hasNotSeparator(): Boolean = !hasSeparator()

    private fun executeName(): String = getPath().substring(getPath().lastIndexOf("/") + 1)

    private fun executeMimeType() = getName().substring(getName().lastIndexOf(".") + 1)

}