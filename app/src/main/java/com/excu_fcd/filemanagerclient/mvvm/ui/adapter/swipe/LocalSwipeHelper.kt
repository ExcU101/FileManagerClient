package com.excu_fcd.filemanagerclient.mvvm.ui.adapter.swipe

import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.excu_fcd.filemanagerclient.mvvm.data.local.LocalUriModel
import com.excu_fcd.filemanagerclient.mvvm.ui.adapter.LocalAdapter
import com.excu_fcd.filemanagerclient.mvvm.ui.adapter.viewholder.LocalViewHolder

class LocalSwipeHelper(private val adapter: LocalAdapter) :
    SwipeHelper<LocalUriModel, LocalViewHolder>(adapter = adapter) {
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    override fun onDraw(
        c: Canvas?,
        recyclerView: RecyclerView?,
        view: View?,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean,
    ) {

    }

    override fun onDrawOver(
        c: Canvas?,
        recyclerView: RecyclerView?,
        view: View?,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean,
    ) {

    }

    override fun clearView(view: View?) {

    }

    override fun onSelected(view: View?) {

    }
}