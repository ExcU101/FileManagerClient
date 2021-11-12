package com.excu_fcd.plugin

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class PluginFragment : Fragment {

    constructor() : super()

    constructor(@LayoutRes layoutId: Int) : super(layoutId)

    fun PluginFragment.getBottomBar() =
        requireActivity().findViewById<View>(com.google.android.material.R.id.accelerate)
}