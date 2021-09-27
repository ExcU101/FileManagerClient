package com.excu_fcd.filemanagerclient.mvvm.data.request

import android.os.Parcelable
import com.excu_fcd.filemanagerclient.mvvm.data.request.type.EmptyOperationType
import com.excu_fcd.filemanagerclient.mvvm.data.request.type.OperationType
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Operation<I>(
    val data: @RawValue List<I>,
    val type: @RawValue OperationType = EmptyOperationType(),
) : Parcelable {

    constructor(item: I, type: @RawValue OperationType = EmptyOperationType()) : this(data = listOf(item), type = type)

}