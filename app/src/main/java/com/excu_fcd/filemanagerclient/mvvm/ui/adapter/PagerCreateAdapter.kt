package com.excu_fcd.filemanagerclient.mvvm.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.excu_fcd.filemanagerclient.mvvm.ui.fragment.NowCreateFragment

class PagerCreateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val fragments = listOf<Fragment>(
        NowCreateFragment(),
        NowCreateFragment()
    )

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}