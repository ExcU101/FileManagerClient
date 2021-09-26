package com.excu_fcd.filemanageclient.mvvm.utils

import android.util.Log
import com.excu_fcd.filemanageclient.mvvm.data.request.Operation
import com.excu_fcd.filemanageclient.mvvm.data.request.Request

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
    type: Operation.Type = Operation.Type.EMPTY,
) {
    add(Operation(item = element, type = type))
}

fun <I> MutableCollection<Operation<I>>.items(
    data: List<I>,
    type: Operation.Type = Operation.Type.EMPTY,
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
