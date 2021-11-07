package com.excu_fcd.filemanagerclient.mvvm.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.excu_fcd.core.extensions.FragmentBindingInitInterface
import com.excu_fcd.filemanagerclient.R
import com.excu_fcd.filemanagerclient.databinding.NavigationLayoutBinding
import com.excu_fcd.filemanagerclient.mvvm.utils.*
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.shape.MaterialShapeDrawable
import kotlin.LazyThreadSafetyMode.NONE

class NavigationFragment : Fragment(), FragmentBindingInitInterface<NavigationLayoutBinding> {

    private var binding: NavigationLayoutBinding? = null
    private val shape by lazy {
        MaterialShapeDrawable(
            topShapeModel(requireContext(), 8)
        ).apply {
            setTint(ContextCompat.getColor(requireContext(), R.color.bottomBarColor))
        }
    }
    private var mFab: FloatingActionButton? = null

    private val behavior by lazy(NONE) {
        binding?.let {
            from(it.navigation)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NavigationLayoutBinding.inflate(inflater, container, false)
        onCreateViewInit(binding!!)
        return binding?.root
    }

    override fun onCreateViewInit(binding: NavigationLayoutBinding) {
        binding.run {
            navigation.background = shape
            scrim.setOnClickListener {
                behavior?.hide()
            }
        }
    }

    fun setFabBehaviorListener(fab: FloatingActionButton) {
        mFab = fab
    }

    fun toggle() {
        if (behavior?.isExpanded()!!) {
            hide()
        } else {
            show()
        }
    }

    fun show() {
        behavior?.show()
    }

    fun hide() {
        behavior?.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.run {

            navigation.setNavigationItemSelectedListener {
                navigation.setCheckedItem(it.itemId)
                when (it.itemId) {
                    R.id.fileManager -> {
                        true
                    }

                    R.id.taskManager -> {
                        true
                    }

                    else -> false
                }
            }

            behavior?.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        STATE_EXPANDED -> {
                            mFab?.hide()
                            requireActivity().findViewById<BottomAppBar>(R.id.bar)
                                .replaceMenu(R.menu.main_menu)
                            scrim.visibility = VISIBLE
                        }

                        STATE_DRAGGING -> {
                            mFab?.hide()
                            scrim.visibility = VISIBLE
                        }

                        STATE_SETTLING -> {
                            mFab?.hide()
                            scrim.visibility = VISIBLE
                        }

                        STATE_HIDDEN -> {
                            mFab?.show()
                            requireActivity().findViewById<BottomAppBar>(R.id.bar)
                                .replaceMenu(R.menu.bar_menu)
                            scrim.visibility = GONE
                        }

                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    scrim.alpha = (slideOffset + 1f) / 2F
                }
            })
        }
    }

}