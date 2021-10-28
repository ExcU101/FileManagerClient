package com.excu_fcd.filemanagerclient.mvvm.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.excu_fcd.core.data.model.DocumentModel
import com.excu_fcd.filemanagerclient.R
import com.excu_fcd.filemanagerclient.mvvm.ui.adapter.listener.OnViewClickListener
import com.excu_fcd.filemanagerclient.mvvm.ui.adapter.viewholder.LocalViewHolder
import com.excu_fcd.filemanagerclient.mvvm.utils.localDiffer
import kotlinx.coroutines.cancel

class LocalAdapter : AbsAdapter<DocumentModel, LocalViewHolder>(localDiffer()) {

    var listener: OnViewClickListener<DocumentModel>? = null

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
            extension.text = (if (item.isDirectory()) "Items: " else "Type: ") + item.getMimeType()
//            size.text = "Size: ${item.fromLength()}"
            icon.setImageResource(if (item.isDirectory()) R.drawable.ic_folder_24 else R.drawable.ic_file_24)
            more.setOnClickListener { view ->
                listener?.onClick(item = item, view = view)
            }
        }
    }
}