package com.excu_fcd.filemanagerclient.mvvm.ui.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.excu_fcd.filemanagerclient.R
import com.excu_fcd.filemanagerclient.databinding.BannerLayoutBinding

class Banner(
    context: Context,
    attrs: AttributeSet
) : CoordinatorLayout(context, attrs) {

    private val binding = BannerLayoutBinding.inflate(LayoutInflater.from(context))

    private var _iconBanner: Drawable? = null
    private var _iconRight: Drawable? = null
    private var _iconLeft: Drawable? = null

    private var _textBanner: String = "Empty text!"
    private var _textRight: String = context.getString(R.string.action_cancel)
    private var _textLeft: String = context.getString(R.string.action_dismiss)

    fun setRightButtonDrawable(drawable: Drawable) {
        _iconRight = drawable
        binding.rightButton.icon = _iconRight
    }

    fun setLeftButtonDrawable(drawable: Drawable) {
        _iconLeft = drawable
        binding.leftButton.icon = _iconLeft
    }

    fun setBannerDrawable(drawable: Drawable) {
        _iconBanner = drawable
        binding.bannerIcon.setImageDrawable(drawable)
    }

    fun setLeftButtonText(text: String) {
        _textLeft = text
        binding.leftButton.text = _textLeft
    }

    fun setRightButtonText(text: String) {
        _textRight = text
        binding.rightButton.text = _textRight
    }

    fun setBannerText(text: String) {
        _textBanner = text
        binding.bannerText.text = _textBanner
    }

    fun dismiss() {
        collapse()
    }

    fun show() {
        expand()
    }

    private fun View.expand() {
        this@expand.measure(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val targetHeight = this@expand.measuredHeight

        this@expand.layoutParams.height = 0
        this@expand.visibility = View.VISIBLE
        val animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                this@expand.layoutParams.height = if (interpolatedTime == 1f)
                    ViewGroup.LayoutParams.WRAP_CONTENT
                else
                    (targetHeight * interpolatedTime).toInt()
                this@expand.requestLayout()
            }

            override fun willChangeBounds(): Boolean = true
        }

        animation.duration =
            (targetHeight / this@expand.context.resources.displayMetrics.density).toInt().toLong()
        this@expand.startAnimation(animation)
    }

    private fun View.collapse() {
        val initialHeight = this.measuredHeight

        val animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    this@collapse.visibility = View.GONE
                } else {
                    this@collapse.layoutParams.height =
                        initialHeight - (initialHeight * interpolatedTime).toInt()
                    this@collapse.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean = true
        }

        animation.duration =
            (initialHeight / this.context.resources.displayMetrics.density).toInt().toLong()
        this.startAnimation(animation)
    }

}