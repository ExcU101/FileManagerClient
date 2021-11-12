package com.excu_fcd.filemanagerclient.mvvm.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.excu_fcd.core.extensions.FragmentBindingInitInterface
import com.excu_fcd.filemanagerclient.R
import com.excu_fcd.filemanagerclient.databinding.TaskFragmentBinding
import com.excu_fcd.filemanagerclient.mvvm.ui.adapter.TaskPagerAdapter
import com.excu_fcd.filemanagerclient.mvvm.viewmodel.TaskViewModel
import com.excu_fcd.plugin.PluginFragment
import kotlin.math.abs

class TaskManagerFragment : PluginFragment(), FragmentBindingInitInterface<TaskFragmentBinding> {

    private val viewModel by hiltNavGraphViewModels<TaskViewModel>(R.id.app_navigation)

    private var binding: TaskFragmentBinding? = null
    private val adapter = TaskPagerAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TaskFragmentBinding.inflate(inflater, container, false)
        onCreateViewInit(binding!!)
        return binding?.root
    }

    override fun onCreateViewInit(binding: TaskFragmentBinding) {
        binding.run {
            taskList.adapter = adapter
            taskList.setPageTransformer { page, position ->
                page.apply {
                    val MIN_SCALE = 0.60f
                    val pageWidth = width
                    when {
                        position < -1 -> {
                            alpha = 0f
                        }
                        position <= 0 -> {
                            alpha = 1f
                            translationX = 0f
                            translationZ = 0f
                            scaleX = 1f
                            scaleY = 1f
                        }
                        position <= 1 -> {
                            alpha = 1 - position
                            translationX = pageWidth * -position
                            translationZ = -1f
                            val scaleFactor =
                                (MIN_SCALE + (1 - MIN_SCALE) * (1 - abs(position)))
                            scaleX = scaleFactor
                            scaleY = scaleFactor
                        }
                        else -> {
                            alpha = 0f
                        }
                    }
                }
            }
        }
    }
}