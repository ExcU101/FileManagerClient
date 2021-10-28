package com.excu_fcd.filemanagerclient.mvvm.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.excu_fcd.core.extensions.asDocumentFile
import com.excu_fcd.core.extensions.asDocumentModel
import com.excu_fcd.filemanagerclient.R
import com.excu_fcd.filemanagerclient.databinding.FilemanagerCreateFragmentBinding
import com.google.android.material.transition.platform.MaterialSharedAxis
import com.google.android.material.transition.platform.MaterialSharedAxis.Z
import java.io.File

class FileManagerCreateFragment : Fragment(R.layout.filemanager_create_fragment) {

    init {
        exitTransition = MaterialSharedAxis(Z, false).apply {
            duration = 300L
        }
        enterTransition = MaterialSharedAxis(Z, true).apply {
            duration = 300L
        }
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FilemanagerCreateFragmentBinding.bind(view)
        binding.run {
            path.setText(requireArguments().getString("currentPath"))
            create.setOnClickListener {
                val file = File(path.text.toString(), name.text.toString())
                val isDir = isDir.isChecked

                val model = file.toUri().asDocumentFile(requireContext())!!.asDocumentModel()

                bundleOf(Pair("createNewLocalFile", model))

                findNavController().popBackStack(R.id.fileManagerFragment, false)
            }
        }
    }

}