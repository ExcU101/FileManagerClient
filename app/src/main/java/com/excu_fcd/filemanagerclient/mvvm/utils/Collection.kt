package com.excu_fcd.filemanagerclient.mvvm.utils

import com.excu_fcd.filemanagerclient.mvvm.data.request.Operation
import com.excu_fcd.filemanagerclient.mvvm.data.request.type.EmptyOperationType
import com.excu_fcd.filemanagerclient.mvvm.data.request.type.OperationType

inline fun <T> list(block: MutableList<T>.() -> Unit): List<T> {
    return mutableListOf<T>().apply(block)
}

fun <T> MutableList<T>.item(item: T) {
    add(item)
}

fun <T> MutableList<T>.typedItem(item: T): T {
    with(item) {
        add(item)
        return this
    }
}

fun <I> MutableCollection<I>.item(element: I) = add(element)

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

fun <T> sortedList(comparator: Comparator<T>, block: MutableList<T>.() -> Unit): List<T> {
    return mutableListOf<T>().apply(block).sortedWith(comparator = comparator)
}

fun sortedList(block: MutableList<String>.() -> Unit): List<String> {
    return mutableListOf<String>().apply(block)
        .sortedWith(comparator = String.CASE_INSENSITIVE_ORDER)
}