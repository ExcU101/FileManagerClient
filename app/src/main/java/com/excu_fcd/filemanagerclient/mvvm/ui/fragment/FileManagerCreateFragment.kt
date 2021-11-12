package com.excu_fcd.filemanagerclient.mvvm.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.excu_fcd.core.data.model.DocumentModel
import com.excu_fcd.core.extensions.asDocumentModel
import com.excu_fcd.filemanagerclient.databinding.FilemanagerCreateFragmentBinding
import com.excu_fcd.filemanagerclient.mvvm.utils.CREATE_KEY
import com.excu_fcd.filemanagerclient.mvvm.utils.item
import com.excu_fcd.filemanagerclient.mvvm.utils.list
import com.excu_fcd.filemanagerclient.mvvm.utils.setPrev
import com.google.android.material.transition.MaterialSharedAxis
import com.google.android.material.transition.MaterialSharedAxis.Y
import java.io.File

class FileManagerCreateFragment : Fragment() {

    private var binding: FilemanagerCreateFragmentBinding? = null

    private val typeAdapter by lazy {
        ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line).apply {
            insert("File", 0)
            insert("Directory", 1)
        }
    }

    fun navigateAndCreate() {
        binding?.let {
            var name = it.name.text.toString()
            val count: Int = if (it.filesCount.text.toString().isEmpty()) 0 else it.filesCount.text.toString().toInt()
            val path = it.path.text.toString()
            val type = typeAdapter.getPosition(it.fileType.text.toString()) == 1
            val extension = it.extension.text.toString()

            if (!extension.contains(".")) {
                extension.replace(extension, ".$extension")
            }

            if (extension != "null" || extension.isNotEmpty()) {
                name += extension
            }

            val file = File(path, name)

            val model = file.asDocumentModel(type)

            if (count != 0) {
                val documents = list<DocumentModel> {
                    repeat(count) { index ->
                        item(File(path, "$name $index$extension").asDocumentModel(type))
                    }
                }

                findNavController().setPrev("CREATE_ARRAY", documents)
                findNavController().popBackStack()
                return
            }

            findNavController().setPrev(CREATE_KEY, model)
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
            fileType.listSelection = 0

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