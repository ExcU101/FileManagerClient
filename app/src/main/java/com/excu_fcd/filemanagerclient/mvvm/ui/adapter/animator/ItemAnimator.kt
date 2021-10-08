package com.excu_fcd.filemanagerclient.mvvm.ui.adapter.animator

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator

class ItemAnimator : SimpleItemAnimator() {
    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder?,
        newHolder: RecyclerView.ViewHolder?,
        fromLeft: Int,
        fromTop: Int,
        toLeft: Int,
        toTop: Int,
    ): Boolean {
        return true
    }

    override fun runPendingAnimations() {

    }

    override fun endAnimation(item: RecyclerView.ViewHolder) {

    }

    override fun endAnimations() {

    }

    override fun isRunning(): Boolean {
        return true
    }

    override fun animateRemove(holder: RecyclerView.ViewHolder?): Boolean {
        return true
    }

    override fun animateAdd(holder: RecyclerView.ViewHolder?): Boolean {
        return true
    }

    override fun animateMove(
        holder: RecyclerView.ViewHolder?,
        fromX: Int,
        fromY: Int,
        toX: Int,
        toY: Int,
    ): Boolean {
        return true
    }
}