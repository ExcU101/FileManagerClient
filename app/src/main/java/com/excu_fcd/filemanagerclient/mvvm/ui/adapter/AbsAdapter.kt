package com.excu_fcd.filemanagerclient.mvvm.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class AbsAdapter<T, VH : RecyclerView.ViewHolder>(
    val differ: DiffUtil.ItemCallback<T>,
) : ListAdapter<T, VH>(differ) {

    val list: MutableList<T> = mutableListOf()

    override fun getCurrentList(): MutableList<T> = list

    override fun getItemCount(): Int {
        return list.size
    }

    fun setData(newData: Collection<T>) {
        list.clear()
        list.addAll(newData)
        notifyItemRangeChanged(0, itemCount)
    }

    fun addData(newData: List<T>) {
        list.addAll(newData)
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
        if (currentList.contains(item)) {
            currentList.add(position, item)
            notifyItemInserted(position)
        }
    }

    fun insertItem(item: T) {
        if (currentList.contains(item)) {
            currentList.add(item)
            notifyItemInserted(getIndex(item))
        }
    }

    private fun getIndex(item: T) = currentList.indexOf(item)

}