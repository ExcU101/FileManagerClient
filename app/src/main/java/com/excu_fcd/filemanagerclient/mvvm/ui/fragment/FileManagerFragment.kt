package com.excu_fcd.filemanagerclient.mvvm.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.excu_fcd.filemanagerclient.R
import com.excu_fcd.filemanagerclient.databinding.FilemanagerFragmentBinding
import com.excu_fcd.filemanagerclient.mvvm.data.Action
import com.excu_fcd.filemanagerclient.mvvm.data.BreadcrumbItem
import com.excu_fcd.filemanagerclient.mvvm.data.local.LocalUriModel
import com.excu_fcd.filemanagerclient.mvvm.data.request.type.DeleteOperationType
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.local.FolderEmptyState
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.local.state.SortedListState
import com.excu_fcd.filemanagerclient.mvvm.ui.adapter.LocalAdapter
import com.excu_fcd.filemanagerclient.mvvm.ui.adapter.listener.OnViewClickListener
import com.excu_fcd.filemanagerclient.mvvm.ui.view.BreadcrumbLayout
import com.excu_fcd.filemanagerclient.mvvm.utils.*
import com.excu_fcd.filemanagerclient.mvvm.viewmodel.LocalViewModel
import com.excu_fcd.filemanagerclient.mvvm.viewmodel.state.LoadingState
import com.excu_fcd.filemanagerclient.mvvm.viewmodel.state.ScreenState
import com.excu_fcd.filemanagerclient.mvvm.viewmodel.state.ViewModelState
import com.google.android.material.transition.platform.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.nio.file.Path

@AndroidEntryPoint
class FileManagerFragment :
    Fragment(R.layout.filemanager_fragment),
    OnViewClickListener<LocalUriModel>, BreadcrumbLayout.Listener {

    private var binding: FilemanagerFragmentBinding? = null

    private val viewModel: LocalViewModel by hiltNavGraphViewModels<LocalViewModel>(R.id.app_navigation)
    private val adapter = LocalAdapter().apply {
        listener = this@FileManagerFragment
    }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            viewModel.updateState()
        }

    private val requestProvideData =
        registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) {
            DocumentFile.fromTreeUri(requireContext(), it)?.listFiles()?.forEach { file ->
                file
            }
        }

    init {
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = 300L
        }
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = 300L
        }
    }

    private val ioScope = CoroutineScope(IO)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        if (savedInstanceState != null) return null
        binding = FilemanagerFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()
        arguments?.getParcelable<LocalUriModel>("createNewLocalFile")?.getName()
            ?.anchoredSnackIt(view)
        if (savedInstanceState == null) {

            binding?.run {
                list.adapter = adapter
                list.setHasFixedSize(true)
                breadcrumbs.setListener(this@FileManagerFragment)
                refresh.setOnRefreshListener {
                    viewModel.refreshState()
                }

                fab.run {
                    setOnClickListener {
                        val extras = FragmentNavigatorExtras(

                        )
                        val bundle = Bundle().apply {
                            putString("currentPath", viewModel.currentPathFlow.value.getPath())
                        }
                        bar.performHide()
                        breadcrumbs.performHide()
                        emptyRoot.visibility = GONE
                        progress.visibility = GONE


                        requestProvideData.launch(Uri.fromFile(Environment.getExternalStorageDirectory()))

//                        findNavController().navigate(
//                            R.id.action_fileManagerFragment_to_fileManagerCreateFragment,
//                            bundle,
//                            null,
//                            extras
//                        )
                    }
                }

                viewLifecycleOwner.lifecycleScope.launch {
                    combine(viewModel.loadingState,
                        viewModel.refreshState,
                        viewModel.currentPathFlow,
                        viewModel.dataState) { isLoading, isRefreshing, currentPath, listState ->
                        ScreenState(isLoading, isRefreshing, currentPath, listState)
                    }.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                        .collect { (isLoading: Boolean, isRefreshing: Boolean, currentPath: LocalUriModel, state: ViewModelState) ->
                            breadcrumbs.setItem(BreadcrumbItem.create(currentPath))
                            refresh.isRefreshing = isRefreshing
                            progress.isIndeterminate = isLoading

                            when (state) {
                                is LoadingState -> {

                                }

                                is FolderEmptyState -> {
                                    list.zeroAlphaAnim()
                                    emptyRoot.visibility = VISIBLE
                                }

                                is SortedListState -> {
                                    adapter.setData(state.list)
                                }
                            }
                        }
                }
            }
        } else {
            arguments?.getParcelable<LocalUriModel>("createNewLocalFile")?.let {
                it.getName().anchoredSnackIt(view)
                ioScope.launch {
                    viewModel.requestCreate(it) { packet ->
                        adapter.onItemResult(packet)
                    }
                }
            }
        }
    }

    override fun navigateTo(model: LocalUriModel) {
        viewModel.updateState(model = model)
    }

    override fun onClick(item: LocalUriModel, view: View) {
        when (view) {
            is LinearLayoutCompat -> {
                if (item.isDirectory()) {
                    navigateTo(item)
                }
            }
            is AppCompatImageButton -> {
                view.popup(items = sortedList(compareBy { it.title }) {
                    item(Action("Add to bookmark", R.drawable.ic_bookmark_24))
                    item(Action("Copy", R.drawable.ic_copy_24))
                    item(Action("Create", R.drawable.ic_add_24))
                    item(Action("Cut", R.drawable.ic_cut_24))
                    item(Action("Delete", R.drawable.ic_delete_24))
                    item(Action("Paste", R.drawable.ic_paste_24))
                }) {
                    when (it.title) {
                        "Delete" -> {
                            ioScope.launch {
                                viewModel.request(request = request {
                                    requestOperations {
                                        item(item, DeleteOperationType)
                                    }
                                    requestId(1)
                                    requestName("Delete ${item.getName()}")
                                }) { packet ->
                                    adapter.onItemResult(packet)
                                }
                            }
                            true
                        }

                        else -> false
                    }
                }.show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}