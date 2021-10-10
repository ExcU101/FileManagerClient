package com.excu_fcd.filemanagerclient.mvvm.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

abstract class FragmentPageAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val fragments: MutableList<Fragment> = mutableListOf()

    fun getFragments(): List<Fragment> = fragments

    fun setFragments(newFragment: Collection<Fragment>) {
        fragments.clear()
        fragments.addAll(newFragment)
        notifyItemRangeInserted(0, itemCount)
    }

    override fun getItemCount(): Int {
        return getFragments().size
    }

    override fun createFragment(position: Int): Fragment {
        return getFragments()[position]
    }
}