package com.excu_fcd.filemanagerclient.mvvm.ui.adapter

import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

abstract class AbsAdapter<T, VH : RecyclerView.ViewHolder>(
    private val callback: DiffUtil.ItemCallback<T>,
) : RecyclerView.Adapter<VH>() {

    protected val scope = CoroutineScope(IO)
    private val differ = Differ(AdapterListUpdateCallback(this), callback)

    val currentList = differ.currentList

    override fun getItemCount(): Int {
        return currentList.size
    }

    fun setData(newData: Collection<T>) {
        differ.currentList = newData.toMutableList()
    }

    fun addData(newData: Collection<T>) {
        scope.launch { currentList.addAll(newData) }
        notifyItemRangeChanged(0, itemCount)
    }

    fun removeItemAt(position: Int) {
        if (position < itemCount) {
            currentList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun removeItem(item: T) {
        if (currentList.contains(item)) {
            removeItemAt(getIndex(item))
        }
    }

    fun insertItemAt(position: Int, item: T) {
        if (!currentList.contains(item)) {
            currentList.add(position, item)
            notifyItemInserted(position)
        }
    }

    fun insertItem(item: T) {
        if (!currentList.contains(item)) {
            currentList.add(item)
            notifyItemInserted(getIndex(item))
        }
    }

    fun changeItem(index: Int, new: T) {
        if (index < currentList.size && index >= 0) {
            currentList[index] = new
            notifyItemChanged(index)
        }
    }

    fun changeItem(old: T, new: T) {
        val index = getIndex(old)

        if (index < currentList.size && index >= 0) {
            currentList[index] = new
            notifyItemChanged(index)
        }
    }

    private fun getIndex(item: T) = currentList.indexOf(item)

}