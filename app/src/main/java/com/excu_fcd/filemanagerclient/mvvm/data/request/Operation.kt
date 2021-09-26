package com.excu_fcd.filemanagerclient.mvvm.data.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Operation<I>(val data: @RawValue List<I>, val type: Type = Type.EMPTY) : Parcelable {

    constructor(item: I, type: Type = Type.EMPTY) : this(data = listOf(item), type = type)

    enum class Type {
        DELETE,
        CREATE,
        CONVERT,
        CUT,
        COPY,
        EMPTY
    }

}