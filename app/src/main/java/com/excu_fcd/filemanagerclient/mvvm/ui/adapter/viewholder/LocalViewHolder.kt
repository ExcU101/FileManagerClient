package com.excu_fcd.filemanagerclient.mvvm.ui.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.excu_fcd.filemanagerclient.databinding.ItemModelLayoutBinding

class LocalViewHolder(private val root: View) : RecyclerView.ViewHolder(root) {

    val binding: ItemModelLayoutBinding = ItemModelLayoutBinding.bind(root)

}