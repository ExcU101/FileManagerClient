package com.excu_fcd.filemanagerclient.mvvm.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.navDeepLink
import com.excu_fcd.filemanagerclient.R
import com.excu_fcd.filemanagerclient.mvvm.data.local.LocalUriModel
import com.excu_fcd.filemanagerclient.mvvm.ui.adapter.viewholder.LocalViewHolder
import com.excu_fcd.filemanagerclient.mvvm.utils.localDiffer

class LocalAdapter : AbsAdapter<LocalUriModel, LocalViewHolder>(localDiffer()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalViewHolder {
        val layout =
            LayoutInflater.from(parent.context).inflate(R.layout.item_model_layout, parent, false)
        return LocalViewHolder(layout)
    }

    override fun onBindViewHolder(holder: LocalViewHolder, position: Int) {
        with(holder.binding) {
            navDeepLink {

            }
        }
    }
}