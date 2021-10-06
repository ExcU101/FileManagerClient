package com.excu_fcd.filemanagerclient.mvvm.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.excu_fcd.filemanagerclient.R
import com.excu_fcd.filemanagerclient.mvvm.data.local.LocalUriModel
import com.excu_fcd.filemanagerclient.mvvm.ui.adapter.listener.OnViewClickListener
import com.excu_fcd.filemanagerclient.mvvm.ui.adapter.viewholder.LocalViewHolder
import com.excu_fcd.filemanagerclient.mvvm.utils.getDrawableIcon
import com.excu_fcd.filemanagerclient.mvvm.utils.localDiffer

class LocalAdapter : AbsAdapter<LocalUriModel, LocalViewHolder>(localDiffer()) {

    var listener: OnViewClickListener<LocalUriModel>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalViewHolder {
        val layout =
            LayoutInflater.from(parent.context).inflate(R.layout.item_model_layout, parent, false)
        return LocalViewHolder(layout)
    }


    override fun onBindViewHolder(holder: LocalViewHolder, position: Int) {
        val item = currentList[position]

        with(holder.binding) {
            root.setOnClickListener { view ->
                listener?.onClick(item = item, view = view)
            }

            title.text = item.getName()
            extension.text = item.extendedExtension
            size.text = "Size: ${item.fromLength()}"
            icon.setImageResource(item.getDrawableIcon())
            more.setOnClickListener { view ->
                listener?.onClick(item = item, view = view)
            }

        }
    }
}