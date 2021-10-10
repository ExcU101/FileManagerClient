package com.excu_fcd.filemanagerclient.mvvm.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.excu_fcd.filemanagerclient.R
import com.excu_fcd.filemanagerclient.databinding.FilemanagerFragmentBinding
import com.excu_fcd.filemanagerclient.mvvm.data.BreadcrumbItem
import com.excu_fcd.filemanagerclient.mvvm.data.local.LocalUriModel
import com.excu_fcd.filemanagerclient.mvvm.data.request.Request
import com.excu_fcd.filemanagerclient.mvvm.data.request.type.DeleteOperationType
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.local.state.SortedListState
import com.excu_fcd.filemanagerclient.mvvm.ui.adapter.LocalAdapter
import com.excu_fcd.filemanagerclient.mvvm.ui.adapter.listener.OnViewClickListener
import com.excu_fcd.filemanagerclient.mvvm.ui.view.BreadcrumbLayout
import com.excu_fcd.filemanagerclient.mvvm.utils.*
import com.excu_fcd.filemanagerclient.mvvm.viewmodel.LocalViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import kotlin.random.Random

@AndroidEntryPoint
class FileManagerFragment :
    Fragment(R.layout.filemanager_fragment),
    OnViewClickListener<LocalUriModel>, BreadcrumbLayout.Listener {

    private val viewModel: LocalViewModel by hiltNavGraphViewModels<LocalViewModel>(R.id.app_navigation)
    private val adapter = LocalAdapter().apply {
        listener = this@FileManagerFragment
    }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            viewModel.update()
        }

    private val ioScope = CoroutineScope(IO)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FilemanagerFragmentBinding.bind(view)
        val context = requireContext()

        with(binding) {
            list.adapter = adapter
            lifecycleScope.launch {
                viewModel.loadingState.flowWithLifecycle(lifecycle).collect {
                    if (it) {
                        list.animate().alpha(0F).start()
                        progress.animate().alpha(1F).start()
                    }
                }
            }

            lifecycleScope.launch {
                viewModel.refreshState.flowWithLifecycle(lifecycle).collect {
                    refresh.isRefreshing = it
                    refresh.setOnRefreshListener {
//                        list.animate().alpha(0F).start()
                        viewModel.refresh()
                    }
                }
            }

            lifecycleScope.launch {
                viewModel.currentPathFlow.flowWithLifecycle(lifecycle).collect {
                    breadcrumbs.setItem(BreadcrumbItem.create(it))
                }
            }

            lifecycleScope.launch {
                viewModel.dataState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).collect {
                    list.animate().alpha(1F).start()
                    progress.animate().alpha(0F).start()
                    when (it) {
                        is SortedListState -> {
                            adapter.setData(it.list)
                            fab.setOnClickListener {
                                adapter.insertItem(File("P ${Random.nextInt()}").asLocalUriModel())
                            }
                        }
                    }
                }
            }
        }
    }

    override fun navigateTo(model: LocalUriModel) {

    }

    override fun onClick(item: LocalUriModel, view: View) {
        when (view) {
            is LinearLayoutCompat -> CoroutineScope(Dispatchers.Default).launch {
                "Linear".snackIt(view = view)
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

}