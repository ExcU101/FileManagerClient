package com.excu_fcd.filemanagerclient.mvvm.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.excu_fcd.filemanagerclient.R
import com.excu_fcd.filemanagerclient.databinding.FilemanagerPagerFragmentBinding
import com.excu_fcd.filemanagerclient.mvvm.ui.adapter.FileManagerPageFragmentAdapter
import com.excu_fcd.filemanagerclient.mvvm.utils.item
import com.excu_fcd.filemanagerclient.mvvm.utils.list
import com.excu_fcd.filemanagerclient.mvvm.utils.snackIt

class FileManagerPagerFragment : Fragment(R.layout.filemanager_pager_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FilemanagerPagerFragmentBinding.bind(view)
        val adapter = FileManagerPageFragmentAdapter(this)

        with(binding) {
            adapter.setFragments(
                list {
                    repeat(3) {
                        item(FileManagerFragment())
                    }
                }
//                listOf(
//                    FileManagerFragment(),
//                    FileManagerFragment(),
//                    FileManagerFragment()
//                )
            )
            pager.adapter = adapter
            pager.setOnClickListener {
                adapter.itemCount.snackIt(it)
            }
        }

    }

}