package com.excu_fcd.filemanagerclient.mvvm.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.HorizontalScrollView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.excu_fcd.core.data.model.DocumentModel
import com.excu_fcd.filemanagerclient.R
import com.excu_fcd.filemanagerclient.databinding.BreadcrumbItemBinding
import com.excu_fcd.filemanagerclient.mvvm.data.Action
import com.excu_fcd.filemanagerclient.mvvm.data.BreadcrumbItem
import com.excu_fcd.filemanagerclient.mvvm.utils.*

class BreadcrumbLayout : HorizontalScrollView {

    private var _item: BreadcrumbItem? = null
    private var _listener: Listener? = null
    private val items: LinearLayoutCompat =
        LinearLayoutCompat(context).apply {
            orientation = LinearLayoutCompat.HORIZONTAL
        }

    private var isShowed = true

    private val backgroundItem = ContextCompat.getDrawable(
        context,
        R.drawable.breadcrumb_item_foreground
    )

    private var isLayoutDirty = false
    private var isScrollToSelectedItemPending = false

    init {
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
//        setMeasuredDimension(widthMeasureSpec, height)
        super.onMeasure(widthMeasureSpec, height)
    }

    override fun requestLayout() {
        isLayoutDirty = true
        super.requestLayout()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        isLayoutDirty = false
        if (isScrollToSelectedItemPending) {
            scrollToSelected()
            isScrollToSelectedItemPending = false
        }
    }

    fun performShow() {
        this.animate().translationY(0F)
        isShowed = true
    }

    fun performHide() {
        this.animate().translationY((-height + -context.dp(16)).toFloat())
        isShowed = false
    }

    fun toggle() {
        if (isShowed) performHide() else performShow()
    }

    private fun scrollToSelected() {
        if (isLayoutDirty) {
            isScrollToSelectedItemPending = true
            return
        }
        _item?.let {
            val selectedItem = items.getChildAt(it.selected)
            val sX = if (layoutDirection == View.LAYOUT_DIRECTION_LTR) {
                selectedItem.left - items.paddingStart
            } else {
                selectedItem.right - width + items.paddingStart
            }
            if (isShown) {
                smoothScrollTo(sX, 0)
            } else {
                scrollTo(sX, 0)
            }
        }
    }

    fun setItem(item: BreadcrumbItem) {
        if (_item != null && item == _item) return
        _item = item
        inflate()
        bind()
        scrollToSelected()
    }

    fun getItem() = _item

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
        _item?.run {
            for (i in models.indices) {
                val binding = items.getChildAt(i).tag as BreadcrumbItemBinding
                binding.text.text = models[i].getName()
                binding.arrow.isVisible = i != models.lastIndex
                binding.root.isActivated = selected == i
                binding.text.setTextColor(
                    ContextCompat.getColor(
                        context,
                        if (selected == i) R.color.breadcrumbSelectedTextColor else R.color.breadcrumbTextColor
                    )
                )
//                if (it.selected == i) {
//                    binding.text.setBackgroundColor(resources.getColor(R.color.ripple))
//                }

                binding.root.setOnLongClickListener { view ->
                    binding.text.popup(
                        items = listOf(
                            Action("Copy", R.drawable.ic_copy_24)
                        )
                    ) {
                        return@popup when (it.title) {
                            "Copy" -> {
                                _listener?.copyPath()
                                true
                            }
                            else -> false
                        }
                    }.show()
                    return@setOnLongClickListener true
                }

                binding.root.setOnClickListener { _ ->
                    if (selected == i) {
                        scrollToSelected()
                    } else {
                        _listener?.navigateTo(model = models[i])
                    }
                }
            }
        }
    }

    fun setListener(listener: Listener) {
        _listener = listener
    }

    interface Listener {
        fun navigateTo(model: DocumentModel)
        fun copyPath()
    }
}