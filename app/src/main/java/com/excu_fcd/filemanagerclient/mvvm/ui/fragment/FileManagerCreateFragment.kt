package com.excu_fcd.filemanagerclient.mvvm.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.excu_fcd.core.extensions.asDocumentFile
import com.excu_fcd.core.extensions.asDocumentModel
import com.excu_fcd.core.extensions.logIt
import com.excu_fcd.filemanagerclient.databinding.FilemanagerCreateFragmentBinding
import com.excu_fcd.filemanagerclient.mvvm.utils.CREATE_KEY
import com.excu_fcd.filemanagerclient.mvvm.utils.set
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.transition.MaterialSharedAxis
import com.google.android.material.transition.MaterialSharedAxis.Y
import java.io.File

class FileManagerCreateFragment : BottomSheetDialogFragment() {

    private var binding: FilemanagerCreateFragmentBinding? = null

    private val typeAdapter by lazy {
        ArrayAdapter<String>(requireContext(), android.R.layout.select_dialog_item).apply {
            insert("File", 0)
            insert("Directory", 1)
        }
    }

    private val behavior: BottomSheetBehavior<*> by lazy { (requireDialog() as BottomSheetDialog).behavior }

    fun navigateAndCreate() {
        binding?.let {
            var name = it.name.text.toString()
            val path = it.path.text.toString()
            val type = it.fileType.listSelection == 1
            val extension = it.extension.text.toString()

            if (!extension.contains(".")) {
                extension.replace(extension, ".$extension")
            }

            if (extension != "null" || extension.isNotEmpty()) {
                name += extension
            }

            val file =
                File(path, name)

            val model = file.toUri().asDocumentFile(requireContext())!!
                .asDocumentModel(type)
            findNavController().set(CREATE_KEY, model)
            findNavController().popBackStack()
        }
    }

    init {
        returnTransition = MaterialSharedAxis(Y, false).apply {
            duration = 300L
        }
        enterTransition = MaterialSharedAxis(Y, true).apply {
            duration = 300L
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FilemanagerCreateFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.run {
            requireArguments().getString("path")?.let {
                path.setText(it)
            }

            fileType.setAdapter(typeAdapter)

            extension.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    if (s.toString() == "png") {
                        inputExtension.helperText = "Supports"
                    } else {
                        inputExtension.helperText = ""
                    }
                }
            })
        }
    }

}