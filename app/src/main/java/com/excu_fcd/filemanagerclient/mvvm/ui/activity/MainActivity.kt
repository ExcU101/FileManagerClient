package com.excu_fcd.filemanagerclient.mvvm.ui.activity

import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.excu_fcd.filemanagerclient.R
import com.excu_fcd.filemanagerclient.databinding.ActivityMainBinding
import com.excu_fcd.filemanagerclient.mvvm.ui.fragment.FileManagerCreateFragment
import com.excu_fcd.filemanagerclient.mvvm.ui.fragment.FileManagerFragment
import com.excu_fcd.filemanagerclient.mvvm.ui.fragment.NavigationFragment
import com.excu_fcd.filemanagerclient.mvvm.utils.*
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomappbar.BottomAppBar.*
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import com.google.android.material.transition.MaterialSharedAxis
import com.google.android.material.transition.MaterialSharedAxis.Y
import dagger.hilt.android.AndroidEntryPoint
import kotlin.LazyThreadSafetyMode.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    private var binding: ActivityMainBinding? = null

    private val navigationFragment: NavigationFragment by lazy(NONE) {
        supportFragmentManager.findFragmentById(R.id.navigationFragment) as NavigationFragment
    }

    private val currentFragment: Fragment?
        get() = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
            ?.childFragmentManager
            ?.fragments
            ?.first()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        if (savedInstanceState == null) {
            binding?.run {
                setContentView(root)
                setupBar(R.menu.bar_menu)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding?.run {
            val controller = findNavController(R.id.nav_host_fragment)
            val icon = AnimatedVectorDrawableCompat.create(
                this@MainActivity,
                R.drawable.anim_menu_to_close
            )

            bar.navigationIcon = icon
            controller.addOnDestinationChangedListener(this@MainActivity)

            bar.setNavigationOnClickListener {
                if (currentFragment is FileManagerCreateFragment) {
                    controller.popBackStack(R.id.fileManagerFragment, false)
                } else {
                    navigationFragment.toggle()
                }
            }

            bar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.refresh -> {
                        if (currentFragment is FileManagerFragment) {
                            (currentFragment as FileManagerFragment).refresh()
                        }
                        true
                    }

                    R.id.backParent -> {
                        if (currentFragment is FileManagerFragment) {
                            (currentFragment as FileManagerFragment).safetyNavigateToParent()
                        }
                        true
                    }

                    else -> false
                }
            }

            navigationFragment.setFabBehaviorListener(fab)

            fab.setOnClickListener {
                when (currentFragment) {
                    is FileManagerFragment -> {
                        navigateToCreate()
                    }

                    is FileManagerCreateFragment -> {
                        navigateFromCreateToManager()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {

            R.id.action_fileManagerFragment_to_taskFragment -> {
                binding?.bar?.menu?.clear()
            }

            R.id.fileManagerFragment -> {
                setupBar(
                    id = R.menu.bar_menu,
                    iconRes = R.drawable.ic_menu_24,
                    mode = FAB_ALIGNMENT_MODE_CENTER,
                    fabIcon = R.drawable.ic_add_24
                )
                binding?.bar?.performShow()
                binding?.fab?.show()
            }

            R.id.taskManagerFragment -> {
                binding?.bar?.menu?.clear()
                setupBar(
                    id = null,
                )
            }

            R.id.fileManagerCreateFragment -> {
                binding?.bar?.performShow()
                setupBar(
                    id = null,
                    iconRes = R.drawable.ic_close_24,
                    mode = FAB_ALIGNMENT_MODE_END,
                    fabIcon = R.drawable.ic_done_24
                )
            }

            R.id.fileManagerPagerFragment -> {
                setupBar(
                    id = R.menu.bar_menu,
                    iconRes = R.drawable.ic_menu_24,
                    mode = FAB_ALIGNMENT_MODE_CENTER
                )
            }
        }
    }

    private fun navigateToCreate() {
        (currentFragment as FileManagerFragment?)?.run {
            exitTransition = MaterialSharedAxis(Y, false).apply {
                duration = 300L
            }
            reenterTransition = MaterialSharedAxis(Y, false).apply {
                duration = 300L
            }
            navigateToCreate()
        }
    }

    private fun navigateFromCreateToManager() {
        (currentFragment as FileManagerCreateFragment)?.run {
            exitTransition = MaterialSharedAxis(Y, false).apply {
                duration = 300L
            }
            reenterTransition = MaterialSharedAxis(Y, false).apply {
                duration = 300L
            }
            navigateAndCreate()
        }
    }

    private fun setupBar(
        @MenuRes id: Int? = null,
        @DrawableRes iconRes: Int? = null,
        @FabAlignmentMode mode: Int? = null,
        @DrawableRes fabIcon: Int? = null
    ) {
        iconRes?.let { binding?.bar?.setNavigationIcon(it) }
        mode?.let { binding?.bar?.fabAlignmentMode = it }
        fabIcon?.let { binding?.fab?.setImageResource(it) }
        if (id != null) {
            binding?.bar?.replaceMenu(id)
        } else {
            binding?.bar?.menu?.clear()
        }
    }

}