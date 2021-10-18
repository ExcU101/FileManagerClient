package com.excu_fcd.filemanagerclient.mvvm.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback

class Differ<T>(
    private val updateCallback: ListUpdateCallback,
    private val callback: DiffUtil.ItemCallback<T>,
) {
    private val _list = mutableListOf<T>()
    var currentList
        get() = _list
        set(newList) {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return currentList.size
                }

                override fun getNewListSize(): Int {
                    return newList.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return callback.areItemsTheSame(currentList[oldItemPosition],
                        newList[newItemPosition])
                }

                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int,
                ): Boolean {
                    return callback.areContentsTheSame(currentList[oldItemPosition],
                        newList[newItemPosition])
                }
            })
            currentList.clear()
            currentList.addAll(newList)
            result.dispatchUpdatesTo(updateCallback)
        }
}