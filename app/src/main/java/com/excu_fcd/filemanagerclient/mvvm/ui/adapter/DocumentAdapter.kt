package com.excu_fcd.filemanagerclient.mvvm.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.excu_fcd.core.data.model.DocumentModel
import com.excu_fcd.core.data.request.operation.Operation
import com.excu_fcd.core.data.request.operation.type.CreateType
import com.excu_fcd.core.data.request.operation.type.DeleteType
import com.excu_fcd.core.extensions.getConcatenatedMimeType
import com.excu_fcd.filemanagerclient.R
import com.excu_fcd.filemanagerclient.mvvm.ui.adapter.listener.OnViewClickListener
import com.excu_fcd.filemanagerclient.mvvm.ui.adapter.viewholder.LocalViewHolder
import com.excu_fcd.filemanagerclient.mvvm.utils.localDiffer
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class DocumentAdapter : AbsAdapter<DocumentModel, LocalViewHolder>(localDiffer()) {

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
            mimeType.text = item.getConcatenatedMimeType()
//            size.text = "Size: ${item.fromLength()}"
            icon.setImageResource(if (item.isDirectory()) R.drawable.ic_folder_24 else R.drawable.ic_file_24)
            more.setOnClickListener { view ->
                listener?.onClick(item = item, view = view)
            }
        }
    }

    fun onSuccessOperation(operation: Operation, item: DocumentModel) {
        MainScope().launch {
            when (operation.type) {
                DeleteType -> {
                    removeItem(item = item)
                }

                CreateType -> {
                    insertItem(item)
                }
            }
        }

    }
}