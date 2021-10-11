package com.excu_fcd.filemanagerclient.mvvm.ui.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.HorizontalScrollView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isVisible
import com.excu_fcd.filemanagerclient.databinding.BreadcrumbItemBinding
import com.excu_fcd.filemanagerclient.mvvm.data.BreadcrumbItem
import com.excu_fcd.filemanagerclient.mvvm.data.local.LocalUriModel
import com.excu_fcd.filemanagerclient.mvvm.utils.atMost
import com.excu_fcd.filemanagerclient.mvvm.utils.dp
import com.excu_fcd.filemanagerclient.mvvm.utils.layoutInflater
import com.excu_fcd.filemanagerclient.mvvm.utils.unspecified

class BreadcrumbLayout : HorizontalScrollView {

    private var _item: BreadcrumbItem? = null
    private var _listener: Listener? = null
    private val items: LinearLayoutCompat by lazy {
        LinearLayoutCompat(context).apply {
            orientation = LinearLayoutCompat.HORIZONTAL
        }
    }

    private var isLayoutDirty = false

    init {
        setBackgroundColor(Color.WHITE)
        isHorizontalScrollBarEnabled = false
        setPaddingRelative(0, 0, 0, 0)
        addView(items, LayoutParams(WRAP_CONTENT, MATCH_PARENT))
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var height = heightMeasureSpec
        if (heightMeasureSpec.unspecified() || heightMeasureSpec.atMost()) {
            val desireHeight = context.dp(48)
            if (heightMeasureSpec.atMost()) {
                height =
                    desireHeight.coerceAtMost(maximumValue = MeasureSpec.getSize(heightMeasureSpec))
            }
            height = MeasureSpec.makeMeasureSpec(desireHeight, MeasureSpec.EXACTLY)
        }
        super.onMeasure(widthMeasureSpec, height)
    }

    override fun requestLayout() {
        isLayoutDirty = true
        super.requestLayout()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        isLayoutDirty = false
    }

    fun scrollToSelected() {
        _item?.let {
            val selected = items.getChildAt(it.selected)

        }
    }

    fun setItem(item: BreadcrumbItem) {
        _item = item
        inflate()
        bind()
    }

    private fun inflate() {
        _item?.let {
            for (i in it.models.size until items.childCount) {
                items.removeViewAt(0)
            }
            for (index in items.childCount until it.models.size) {
                val binding = BreadcrumbItemBinding.inflate(context.layoutInflater, items, false)
                binding.root.tag = binding
                items.addView(binding.root, 0)
            }
        }
    }

    private fun bind() {
        _item?.let {
            for (i in it.models.indices) {
                val binding = items.getChildAt(i).tag as BreadcrumbItemBinding
                binding.text.text = it.models[i].getName()
                binding.arrow.isVisible = i != it.models.size - 1
                binding.root.setOnClickListener { _ ->
                    if (it.selected == i) {
                        scrollToSelected()
                    } else {
                        _listener?.navigateTo(it.models[i])
                    }
                }
            }
        }
    }

    fun setListener(listener: Listener) {
        _listener = listener
    }

    interface Listener {
        fun navigateTo(model: LocalUriModel)
    }
}