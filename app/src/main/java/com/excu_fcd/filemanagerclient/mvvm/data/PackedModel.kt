package com.excu_fcd.filemanagerclient.mvvm.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PackedModel(
    private val name: String,
    private val mimeType: String,
    private val isDirectory: Boolean,
    private val isHidden: Boolean = true,
    private val canRead: Boolean = true,
    private val canWrite: Boolean = true,
) : Parcelable {

}