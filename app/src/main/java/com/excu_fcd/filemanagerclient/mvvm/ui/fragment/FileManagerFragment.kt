package com.excu_fcd.filemanagerclient.mvvm.ui.fragment

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.excu_fcd.core.data.model.DocumentModel
import com.excu_fcd.core.data.request.operation.Operation
import com.excu_fcd.core.data.request.operation.OperationDataEmptyReason
import com.excu_fcd.core.data.request.operation.operation
import com.excu_fcd.core.data.request.operation.reason.Reason
import com.excu_fcd.core.data.request.operation.reason.TextualReason
import com.excu_fcd.core.data.request.priority.Priority
import com.excu_fcd.core.data.request.requestBuilder
import com.excu_fcd.core.provider.job.Job
import com.excu_fcd.filemanagerclient.R
import com.excu_fcd.filemanagerclient.databinding.FilemanagerFragmentBinding
import com.excu_fcd.filemanagerclient.mvvm.data.Action
import com.excu_fcd.filemanagerclient.mvvm.data.BreadcrumbItem
import com.excu_fcd.filemanagerclient.mvvm.ui.adapter.LocalAdapter
import com.excu_fcd.filemanagerclient.mvvm.ui.adapter.listener.OnViewClickListener
import com.excu_fcd.filemanagerclient.mvvm.ui.view.BreadcrumbLayout
import com.excu_fcd.filemanagerclient.mvvm.utils.*
import com.excu_fcd.filemanagerclient.mvvm.viewmodel.DocumentViewModel
import com.excu_fcd.filemanagerclient.mvvm.viewmodel.state.ListState
import com.excu_fcd.filemanagerclient.mvvm.viewmodel.state.ViewModelState
import com.google.android.material.transition.platform.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FileManagerFragment :
    Fragment(R.layout.filemanager_fragment),
    OnViewClickListener<DocumentModel>, BreadcrumbLayout.Listener {

    private var binding: FilemanagerFragmentBinding? = null

    private val viewModel: DocumentViewModel by hiltNavGraphViewModels(R.id.app_navigation)
    private val adapter = LocalAdapter().apply {
        listener = this@FileManagerFragment
    }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {

        }

    private val requestProvideData =
        registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) {

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
        arguments?.getParcelable<DocumentModel>("createNewLocalFile")?.getName()
            ?.anchoredSnackIt(view)
        if (savedInstanceState == null) {

            binding?.run {
                list.adapter = adapter
                list.setHasFixedSize(true)
                breadcrumbs.setListener(this@FileManagerFragment)
                progress.hide()
                refresh.setOnRefreshListener {

                }

                fab.setOnClickListener {
                    requestPermission
                    requestProvideData.launch(Environment.getDataDirectory().toUri())
                }

                lifecycleScope.launch {
                    combine(viewModel.stateFlow, viewModel.currentPathFlow) { t1, t2 ->
                        Pair(t1, t2)
                    }.flowWithLifecycle(lifecycle = lifecycle)
                        .collect { (state: ViewModelState, currentPath: DocumentModel) ->
                            breadcrumbs.setItem(BreadcrumbItem.create(currentPath))
                            if (state is ListState) {
                                state.list.forEach {
                                    it.getName().logIt()
                                }
                                adapter.setData(state.list)
                            }
                        }
                }
            }
        }
    }

    override fun navigateTo(model: DocumentModel) {
        viewModel.updateState(path = model)
    }

    override fun onClick(item: DocumentModel, view: View) {
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
                                viewModel.request(request = requestBuilder {
                                    requestOperations {
                                        operation {
                                            data {
                                                item(item)
                                            }
                                            priority(Priority.middle())
                                            type(Operation.Type.delete())
                                        }
                                        requestPriority(Priority.middle())
                                        requestName("Delete ${item.getName()}")
                                    }
                                }, object : Job.OperationCallback {
                                    override fun onOperationTypeGranted(operation: Operation) {

                                    }

                                    override fun onOperationTypeDenied(
                                        operation: Operation,
                                        reason: Reason,
                                    ) {
                                        when (reason) {
                                            is TextualReason -> {
                                                reason.text.logIt()
                                            }
                                            is OperationDataEmptyReason -> {
                                                reason.logIt()
                                            }
                                        }
                                        "Denied ${operation.type}".logIt()
                                    }

                                }, object : Job.ItemOperationCallback {
                                    override fun onOperationWork(operation: Operation) {

                                    }

                                    override fun onItemOperationSuccess(
                                        model: DocumentModel,
                                        reason: Reason,
                                    ) {
                                        adapter.removeItem(model)
                                        when (reason) {
                                            is TextualReason -> {
                                                reason.text.logIt()
                                            }
                                        }
                                    }

                                    override fun onItemOperationFailure(
                                        model: DocumentModel,
                                        reason: Reason,
                                    ) {
                                        when (reason) {
                                            is TextualReason -> {
                                                reason.text.logIt()
                                            }
                                        }
                                    }

                                })
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