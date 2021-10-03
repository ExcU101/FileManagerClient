package com.excu_fcd.filemanagerclient.mvvm.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.excu_fcd.filemanagerclient.databinding.FilemanagerFragmentBinding
import com.excu_fcd.filemanagerclient.mvvm.ui.adapter.LocalAdapter

class FileManagerFragment : Fragment() {

    private var binding: FilemanagerFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FilemanagerFragmentBinding.inflate(layoutInflater, null, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            list.adapter = LocalAdapter()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}