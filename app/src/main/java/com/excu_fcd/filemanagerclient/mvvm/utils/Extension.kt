package com.excu_fcd.filemanagerclient.mvvm.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.MeasureSpec.*
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.appcompat.widget.IconPopupMenu
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asFlow
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.excu_fcd.core.data.model.DocumentModel
import com.excu_fcd.filemanagerclient.mvvm.data.Action
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import com.google.android.material.navigation.NavigationView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.math.max


const val localBookmark = "localBookmark"
const val remoteBookmark = "remoteBookmark"

internal const val CREATE_KEY = "CREATE"

internal const val createNewLocalFile: String = "createNewLocalFile"


fun <T> NavController.getLiveData(key: String) =
    currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)

fun <T> NavController.getLiveDataPrev(key: String) =
    previousBackStackEntry?.savedStateHandle?.getLiveData<T>(key)

fun <T> NavController.setPrev(key: String, value: T) =
    previousBackStackEntry?.savedStateHandle?.set(key, value)

fun <T> NavController.getPrev(key: String) =
    previousBackStackEntry?.savedStateHandle?.get<T>(key)

fun <T> NavController.set(key: String, value: T) =
    currentBackStackEntry?.savedStateHandle?.set(key, value)

fun <T> NavController.get(key: String, value: T) =
    currentBackStackEntry?.savedStateHandle?.get<T>(key)

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

fun NavigationView.replaceMenu(@MenuRes id: Int) {
    menu.clear()
    inflateMenu(id)
}

fun CircularProgressIndicator.toggle(isLoading: Boolean) {
    if (isLoading) show() else hide()
}

fun Context.dp(value: Int): Int {
    return (resources.displayMetrics.density * value).toInt()
}

fun Fragment.finish() {
    requireActivity().finish()
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

fun localDiffer(): DiffUtil.ItemCallback<DocumentModel> =
    object : DiffUtil.ItemCallback<DocumentModel>() {
        override fun areItemsTheSame(oldItem: DocumentModel, newItem: DocumentModel): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: DocumentModel, newItem: DocumentModel): Boolean {
            return oldItem.getScheme() == newItem.getScheme() && oldItem.getName() == newItem.getName()
        }

    }

fun Context.uriPackage() = Uri.fromParts("package", packageName, null)

fun RecyclerView.touchHelper(callback: ItemTouchHelper.Callback) {
    ItemTouchHelper(callback).attachToRecyclerView(this)
}

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

fun <T : Any> T.anchoredSnackIt(
    view: View,
    anchorView: View = view,
    duration: Int = Snackbar.LENGTH_SHORT,
    animationMode: Int = Snackbar.ANIMATION_MODE_FADE,
): T {
    Snackbar.make(view, "$this", duration).setAnimationMode(animationMode)
        .setAnchorView(anchorView).show()
    return this
}

fun BottomSheetBehavior<*>.hide() {
    state = STATE_HIDDEN
}

fun BottomSheetBehavior<*>.show() {
    state = STATE_EXPANDED
}

fun BottomSheetBehavior<*>.toggle() {
    if (state == STATE_EXPANDED) {
        return hide()
    }
    return show()
}

fun topShapeModel(context: Context, value: Int) =
    ShapeAppearanceModel.builder().setTopRightCornerSize(context.dp(value).toFloat())
        .setTopLeftCornerSize(context.dp(value).toFloat()).build()

fun BottomSheetBehavior<*>.isExpanded() = state == STATE_EXPANDED
fun BottomSheetBehavior<*>.isHidden() = state == STATE_HIDDEN

fun <T : Any> T.snackIt(
    view: View,
    duration: Int = Snackbar.LENGTH_SHORT,
    animationMode: Int = Snackbar.ANIMATION_MODE_SLIDE,
): T {
    Snackbar.make(view, "$this", duration).setAnimationMode(animationMode)
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