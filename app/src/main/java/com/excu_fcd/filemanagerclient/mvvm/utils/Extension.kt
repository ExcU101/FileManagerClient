package com.excu_fcd.filemanagerclient.mvvm.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.MeasureSpec.*
import android.widget.Toast
import androidx.appcompat.widget.IconPopupMenu
import androidx.appcompat.widget.PopupMenu
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asFlow
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.excu_fcd.filemanagerclient.R
import com.excu_fcd.filemanagerclient.mvvm.data.Action
import com.excu_fcd.filemanagerclient.mvvm.data.local.LocalUriModel
import com.excu_fcd.filemanagerclient.mvvm.data.request.Request
import com.excu_fcd.filemanagerclient.mvvm.feature.worker.Worker
import com.excu_fcd.filemanagerclient.mvvm.feature.worker.result.Failure
import com.excu_fcd.filemanagerclient.mvvm.feature.worker.result.Result
import com.excu_fcd.filemanagerclient.mvvm.feature.worker.result.Success
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import kotlin.math.max


const val localBookmark = "localBookmark"
const val remoteBookmark = "remoteBookmark"

internal const val createNewLocalFile: String = "createNewLocalFile"

fun View.makeMeasure(spec: Int, desire: Int): Int {
    return when {
        spec.exactly() -> {
            spec.getSize()
        }
        spec.atMost() -> {
            max(spec.getSize(), desire)
        }
        else -> {
            desire
        }
    }
}

fun Context.dp(value: Int): Int {
    return (resources.displayMetrics.density * value).toInt()
}

fun Int.getMode() = View.MeasureSpec.getMode(this)
fun Int.getSize() = View.MeasureSpec.getSize(this)
fun Int.atMost() = getMode() == AT_MOST
fun Int.exactly() = getMode() == EXACTLY
fun Int.unspecified() = getMode() == UNSPECIFIED

val Context.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(this)

fun View.zeroAlphaAnim() = animate().alpha(0F).start()
fun View.oneAlphaAnim() = animate().alpha(1F).start()

fun localDiffer(): DiffUtil.ItemCallback<LocalUriModel> =
    object : DiffUtil.ItemCallback<LocalUriModel>() {
        override fun areItemsTheSame(oldItem: LocalUriModel, newItem: LocalUriModel): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: LocalUriModel, newItem: LocalUriModel): Boolean {
            return oldItem.getExtension() == newItem.getExtension() && oldItem.getName() == newItem.getName()
        }

    }

fun File.asLocalUriModel() = LocalUriModel(toUri())

fun Context.uriPackage() = Uri.fromParts("package", packageName, null)

fun RecyclerView.touchHelper(callback: ItemTouchHelper.Callback) {
    ItemTouchHelper(callback).attachToRecyclerView(this)
}


val Worker<*, *>.workerScope
    get() = CoroutineScope(IO)

fun Result.isSuccess() = this is Success
fun Result.isFailure() = this is Failure

fun View.popup(
    items: List<Action> = emptyList(),
    listener: PopupMenu.OnMenuItemClickListener = PopupMenu.OnMenuItemClickListener { false },
): PopupMenu {
    val popupMenu = IconPopupMenu(context, this)
    items.forEach {
        popupMenu.menu.add(it.title).apply {
            setIcon(it.icon)
        }
    }
    popupMenu.setOnMenuItemClickListener(listener)
    return popupMenu
}

fun LocalUriModel.getDrawableIcon(): Int =
    if (isDirectory() && !isImage()) R.drawable.ic_folder_24 else R.drawable.ic_file_24

fun LocalUriModel.getTypedExtension() =
    (if (isDirectory()) "Items: " else "Type: ") + getExtension()

fun Uri.asLocalUriModel() = LocalUriModel(uri = this)
fun DocumentFile.asLocalUriModel() = LocalUriModel(uri = uri)

fun <T : Any> T.anchoredSnackIt(
    view: View,
    anchorView: View = view,
    duration: Int = Snackbar.LENGTH_SHORT,
    animationMode: Int = Snackbar.ANIMATION_MODE_SLIDE,
): T {
    Snackbar.make(view, "Snack it! ($javaClass) $this", duration).setAnimationMode(animationMode)
        .setAnchorView(anchorView).show()
    return this
}

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

fun <T> SavedStateHandle.getStateFlow(
    scope: CoroutineScope,
    key: String,
    initialValue: T,
): MutableStateFlow<T> {
    val savedStateHandle = this
    val flow = MutableStateFlow(initialValue)

    scope.launch {
        flow.collect {
            savedStateHandle[key] = it
        }
    }

    scope.launch {
        savedStateHandle.getLiveData<T>(key).asFlow().collect {
            flow.value = it
        }
    }
    return flow
}

@DslMarker
annotation class RequestBuilderMarker

fun <I> request(block: Request.Builder<I> .() -> Unit): Request<I> {
    return Request.Builder<I>().apply(block).build()
}