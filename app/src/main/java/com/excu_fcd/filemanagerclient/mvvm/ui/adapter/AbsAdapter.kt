package com.excu_fcd.filemanagerclient.mvvm.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

abstract class AbsAdapter<T, VH : RecyclerView.ViewHolder>(
    differ: DiffUtil.ItemCallback<T>,
) : ListAdapter<T, VH>(differ) {

    val list: MutableList<T> = mutableListOf()

    protected val scope = CoroutineScope(IO)

    override fun getCurrentList(): MutableList<T> = list

    override fun getItemCount(): Int {
        return list.size
    }

    fun setData(newData: Collection<T>) {
        scope.launch {
            list.clear()
            list.addAll(newData)
        }
        notifyItemRangeChanged(0, list.size)
    }

    fun addData(newData: List<T>) {
        scope.launch { list.addAll(newData) }
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

    private fun getIndex(item: T) = currentList.indexOf(item)

}