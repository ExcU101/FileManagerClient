package com.excu_fcd.filemanagerclient.mvvm.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.excu_fcd.filemanagerclient.R
import com.excu_fcd.filemanagerclient.mvvm.data.local.LocalUriModel
import com.excu_fcd.filemanagerclient.mvvm.data.request.type.CreateOperationType
import com.excu_fcd.filemanagerclient.mvvm.data.request.type.DeleteOperationType
import com.excu_fcd.filemanagerclient.mvvm.feature.LocalEventPack
import com.excu_fcd.filemanagerclient.mvvm.ui.adapter.listener.OnViewClickListener
import com.excu_fcd.filemanagerclient.mvvm.ui.adapter.viewholder.LocalViewHolder
import com.excu_fcd.filemanagerclient.mvvm.utils.getDrawableIcon
import com.excu_fcd.filemanagerclient.mvvm.utils.isSuccess
import com.excu_fcd.filemanagerclient.mvvm.utils.localDiffer
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class LocalAdapter : AbsAdapter<LocalUriModel, LocalViewHolder>(localDiffer()) {

    var listener: OnViewClickListener<LocalUriModel>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalViewHolder {
        val layout =
            LayoutInflater.from(parent.context).inflate(R.layout.item_model_layout, parent, false)
        return LocalViewHolder(layout)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        scope.cancel(message = "View detached from Recycler View")
    }

    override fun onBindViewHolder(holder: LocalViewHolder, position: Int) {
        val item = currentList[position]
        with(holder.binding) {
            itemRoot.setOnClickListener { view ->
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

    fun onItemResult(pack: LocalEventPack) {
        when (pack.type) {
            is DeleteOperationType -> {
                if (pack.result.isSuccess()) {
                    MainScope().launch { removeItem(item = pack.item) }
                }
            }

            is CreateOperationType -> {
                if (pack.result.isSuccess()) {
                    MainScope().launch { insertItem(item = pack.item) }
                }
            }
        }
    }
}