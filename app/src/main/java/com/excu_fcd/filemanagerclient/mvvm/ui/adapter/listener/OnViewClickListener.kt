package com.excu_fcd.filemanagerclient.mvvm.ui.adapter.listener

import android.view.View

interface OnViewClickListener<T> {

    fun onClick(item: T, view: View)

}