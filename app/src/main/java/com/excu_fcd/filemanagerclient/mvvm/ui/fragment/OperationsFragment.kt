package com.excu_fcd.filemanagerclient.mvvm.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResult
import com.excu_fcd.core.data.model.DocumentModel
import com.excu_fcd.core.extensions.FragmentBindingInitInterface
import com.excu_fcd.filemanagerclient.R
import com.excu_fcd.filemanagerclient.databinding.OperationsLayoutBinding
import com.excu_fcd.filemanagerclient.mvvm.utils.hide
import com.excu_fcd.filemanagerclient.mvvm.utils.topShapeModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.shape.MaterialShapeDrawable

class OperationsFragment : BottomSheetDialogFragment(),
    FragmentBindingInitInterface<OperationsLayoutBinding> {

    private var binding: OperationsLayoutBinding? = null

    private val shape by lazy {
        MaterialShapeDrawable(
            topShapeModel(requireContext(), 8)
        ).apply {
            setTint(ContextCompat.getColor(requireContext(), R.color.bottomBarColor))
        }
    }

    private val behavior by lazy {
        (dialog as BottomSheetDialog?)?.behavior
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = OperationsLayoutBinding.inflate(inflater, container, false)
        onCreateViewInit(binding!!)
        return binding?.root
    }

    override fun onCreateViewInit(binding: OperationsLayoutBinding) {
        binding.run {
            operations.background = shape
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.run {

            operations.setNavigationItemSelectedListener { item ->
                arguments?.getParcelable<DocumentModel>("item")?.let {
                    setFragmentResult("operations", Bundle().apply {
                        putString("operation", item.title.toString())
                        putParcelable("item", it)
                    })
                }
                behavior?.hide()
                true
            }
        }
    }

}