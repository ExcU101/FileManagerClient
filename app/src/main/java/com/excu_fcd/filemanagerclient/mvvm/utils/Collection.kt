package com.excu_fcd.filemanagerclient.mvvm.utils

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