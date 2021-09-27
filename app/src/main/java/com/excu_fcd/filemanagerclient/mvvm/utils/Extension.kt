package com.excu_fcd.filemanagerclient.mvvm.utils

import android.util.Log
import com.excu_fcd.filemanagerclient.mvvm.data.request.Operation
import com.excu_fcd.filemanagerclient.mvvm.data.request.Request
import com.excu_fcd.filemanagerclient.mvvm.data.request.type.EmptyOperationType
import com.excu_fcd.filemanagerclient.mvvm.data.request.type.OperationType

fun <T : Any> T.logIt(): T {
    Log.i("Loggable ($javaClass)", this.toString())
    return this
}

fun String.logIt(): String {
    Log.i("Loggable ($javaClass)", this)
    return this
}

fun <I> MutableCollection<I>.item(element: I) {
    add(element)
}

fun <I> MutableCollection<Operation<I>>.item(
    element: I,
    type: OperationType = EmptyOperationType(),
) {
    add(Operation(item = element, type = type))
}

fun <I> MutableCollection<Operation<I>>.items(
    data: List<I>,
    type: OperationType = EmptyOperationType(),
) {
    add(Operation(data = data, type = type))
}

fun <I> MutableCollection<I>.items(elements: Collection<I>) {
    addAll(elements)
}

@DslMarker
annotation class RequestBuilderMarker

fun <I> request(block: Request.Builder<I> .() -> Unit): Request<I> {
    return Request.Builder<I>().apply(block).build()
}
