package com.excu_fcd.filemanagerclient.mvvm.utils

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import com.excu_fcd.filemanagerclient.R
import com.excu_fcd.filemanagerclient.mvvm.data.local.LocalUriModel
import com.excu_fcd.filemanagerclient.mvvm.data.request.Operation
import com.excu_fcd.filemanagerclient.mvvm.data.request.Request
import com.excu_fcd.filemanagerclient.mvvm.data.request.type.EmptyOperationType
import com.excu_fcd.filemanagerclient.mvvm.data.request.type.OperationType
import com.google.android.material.snackbar.Snackbar


fun localDiffer(): DiffUtil.ItemCallback<LocalUriModel> =
    object : DiffUtil.ItemCallback<LocalUriModel>() {
        override fun areItemsTheSame(oldItem: LocalUriModel, newItem: LocalUriModel): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: LocalUriModel, newItem: LocalUriModel): Boolean {
            return oldItem.getExtension() == newItem.getExtension() && oldItem.getName() == newItem.getName()
        }

    }

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

fun <T> sortedList(comparator: Comparator<T>, block: MutableList<T>.() -> Unit): List<T> {
    return mutableListOf<T>().apply(block).sortedWith(comparator = comparator)
}

fun sortedList(block: MutableList<String>.() -> Unit): List<String> {
    return mutableListOf<String>().apply(block)
        .sortedWith(comparator = String.CASE_INSENSITIVE_ORDER)
}

fun View.popup(
    items: List<String> = emptyList(),
    listener: PopupMenu.OnMenuItemClickListener = PopupMenu.OnMenuItemClickListener { false },
): PopupMenu {
    val popupMenu = PopupMenu(context, this)
    items.forEach {
        popupMenu.menu.add(it)
    }
    popupMenu.setOnMenuItemClickListener(listener)
    return popupMenu
}

fun LocalUriModel.getDrawableIcon(): Int =
    if (isDirectory() && !isImage()) R.drawable.ic_folder_24 else R.drawable.ic_file_24

fun LocalUriModel.getTypedExtension() =
    (if (isDirectory()) "Items: " else "Type: ") + getExtension()

fun <T : Any> T.snackIt(
    view: View,
    duration: Int = Snackbar.LENGTH_SHORT,
    animationMode: Int = Snackbar.ANIMATION_MODE_SLIDE,
): T {
    Snackbar.make(view, "Snack it! ($javaClass) $this", duration).setAnimationMode(animationMode)
        .show()
    return this
}

fun <T : Any> T.toastIt(
    context: Context,
    duration: Int = Toast.LENGTH_SHORT,
): T {
    Toast.makeText(context, "Toast it! ($javaClass) $this", duration)
        .show()
    return this
}

fun <T : Any> T.logIt(): T {
    Log.i("Loggable ($javaClass)", this.toString())
    return this
}

fun String.logIt(): String {
    Log.i("Loggable ($javaClass)", this)
    return this
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

@DslMarker
annotation class RequestBuilderMarker

fun <I> request(block: Request.Builder<I> .() -> Unit): Request<I> {
    return Request.Builder<I>().apply(block).build()
}