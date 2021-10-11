package com.excu_fcd.filemanagerclient.mvvm.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.excu_fcd.filemanagerclient.R
import com.excu_fcd.filemanagerclient.databinding.FilemanagerFragmentBinding
import com.excu_fcd.filemanagerclient.mvvm.data.BreadcrumbItem
import com.excu_fcd.filemanagerclient.mvvm.data.local.LocalUriModel
import com.excu_fcd.filemanagerclient.mvvm.data.request.Request
import com.excu_fcd.filemanagerclient.mvvm.data.request.type.DeleteOperationType
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.local.FolderEmptyState
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.local.state.SortedListState
import com.excu_fcd.filemanagerclient.mvvm.ui.adapter.LocalAdapter
import com.excu_fcd.filemanagerclient.mvvm.ui.adapter.listener.OnViewClickListener
import com.excu_fcd.filemanagerclient.mvvm.ui.view.BreadcrumbLayout
import com.excu_fcd.filemanagerclient.mvvm.utils.*
import com.excu_fcd.filemanagerclient.mvvm.viewmodel.LocalViewModel
import com.excu_fcd.filemanagerclient.mvvm.viewmodel.state.ScreenState
import com.excu_fcd.filemanagerclient.mvvm.viewmodel.state.ViewModelState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

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
            viewModel.update()
        }

    private val ioScope = CoroutineScope(IO)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FilemanagerFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()
        with(binding!!) {
            list.adapter = adapter
            refresh.setOnRefreshListener {
                viewModel.refresh()
            }

            viewLifecycleOwner.lifecycleScope.launch {
                combine(viewModel.loadingState,
                    viewModel.refreshState,
                    viewModel.currentPathFlow,
                    viewModel.fileListState) { isLoading, isRefreshing, currentPath, listState ->
                    ScreenState(isLoading, isRefreshing, currentPath, listState)
                }.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                    .collect { (isLoading: Boolean, isRefreshing: Boolean, currentPath: LocalUriModel, listState: ViewModelState) ->
                        breadcrumbs.setItem(BreadcrumbItem.create(currentPath))
                        refresh.isRefreshing = isRefreshing
                        if (isLoading) {
                            progress.oneAlphaAnim()
                            list.zeroAlphaAnim()
                        }
                        if (isRefreshing) {
                            list.zeroAlphaAnim()
                        } else {
                            list.oneAlphaAnim()
                        }
                        when (listState) {
                            is SortedListState -> {
                                list.oneAlphaAnim()
                                progress.zeroAlphaAnim()
                                adapter.setData(listState.list)
                            }
                            is FolderEmptyState -> {
                                list.zeroAlphaAnim()
                                "Folder is empty".snackIt(view)
                            }
                        }
                    }
            }
        }
    }

    override fun navigateTo(model: LocalUriModel) {
        binding?.breadcrumbs?.setItem(BreadcrumbItem.create(model))
        viewModel.updateFromFile(file = model.getFile())
    }

    override fun onClick(item: LocalUriModel, view: View) {
        when (view) {
            is LinearLayoutCompat -> {
                if (item.isDirectory()) {
                    navigateTo(item)
                }
            }
            is AppCompatImageButton -> {
                view.popup(items = sortedList {
                    item("Add to bookmark")
                    item("Copy")
                    item("Create")
                    item("Cut")
                    item("Delete")
                    item("Paste")
                }) {
                    when (it.title) {
                        "Delete" -> {
                            ioScope.launch {
                                val request: Request<LocalUriModel> = request {
                                    requestName("Delete ${item.getName()}")
                                    requestId(2)
                                    requestOperations {
                                        item(element = item, type = DeleteOperationType())
                                    }
                                }
                                viewModel.request(request = request) { result ->
                                    adapter.onResponse(operations = request.getOperations(), result)
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