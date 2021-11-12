package com.excu_fcd.core.data.model

import android.os.Parcel
import androidx.documentfile.provider.DocumentFile
import kotlinx.parcelize.Parceler
import java.io.File

object ParceledFile : Parceler<DocumentFile> {

    override fun DocumentFile.write(parcel: Parcel, flags: Int) {
        parcel.writeString(uri.path)
    }

    override fun create(parcel: Parcel): DocumentFile {
        return DocumentFile.fromFile(File(parcel.readString()))
    }

}