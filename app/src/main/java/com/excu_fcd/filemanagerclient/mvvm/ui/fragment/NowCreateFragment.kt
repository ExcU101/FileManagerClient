package com.excu_fcd.filemanagerclient.mvvm.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.excu_fcd.filemanagerclient.databinding.CreateNowFragmentBinding

class NowCreateFragment : Fragment() {

    private var binding: CreateNowFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = CreateNowFragmentBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding!!) {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}