package com.excu_fcd.filemanagerclient.mvvm.ui.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.excu_fcd.core.data.model.DocumentModel
import com.excu_fcd.core.data.request.Request
import com.excu_fcd.core.data.request.operation.Operation
import com.excu_fcd.core.data.request.operation.reason.Reason
import com.excu_fcd.core.data.request.operation.reason.TextualReason
import com.excu_fcd.core.extensions.*
import com.excu_fcd.core.extensions.logIt
import com.excu_fcd.core.provider.job.callback.OperationJobCallback
import com.excu_fcd.core.provider.job.callback.RenameModelJobCallback
import com.excu_fcd.filemanagerclient.R
import com.excu_fcd.filemanagerclient.databinding.ActivityMainBinding
import com.excu_fcd.filemanagerclient.databinding.FilemanagerFragmentBinding
import com.excu_fcd.filemanagerclient.databinding.OperationsLayoutBinding
import com.excu_fcd.filemanagerclient.mvvm.data.BreadcrumbItem
import com.excu_fcd.filemanagerclient.mvvm.ui.adapter.DocumentAdapter
import com.excu_fcd.filemanagerclient.mvvm.ui.adapter.listener.OnViewClickListener
import com.excu_fcd.filemanagerclient.mvvm.ui.view.BreadcrumbLayout
import com.excu_fcd.filemanagerclient.mvvm.utils.*
import com.excu_fcd.filemanagerclient.mvvm.viewmodel.DocumentViewModel
import com.excu_fcd.filemanagerclient.mvvm.viewmodel.state.ListState
import com.excu_fcd.filemanagerclient.mvvm.viewmodel.state.ScreenState
import com.excu_fcd.filemanagerclient.mvvm.viewmodel.state.ViewModelState
import com.excu_fcd.plugin.PluginFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlin.LazyThreadSafetyMode.*


@AndroidEntryPoint
class FileManagerFragment :
    PluginFragment(),
    OnViewClickListener<DocumentModel>,
    BreadcrumbLayout.Listener, FragmentResultListener,
    FragmentBindingInitInterface<FilemanagerFragmentBinding> {

    private var binding: FilemanagerFragmentBinding? = null

    private val viewModel: DocumentViewModel by hiltNavGraphViewModels(R.id.app_navigation)
    private val adapter = DocumentAdapter().apply {
        listener = this@FileManagerFragment
    }

    private val fab by lazy(NONE) {
        requireActivity().findViewById<FloatingActionButton>(R.id.fab)
    }

    private val operationsBinding by lazy {
        OperationsLayoutBinding.inflate(layoutInflater)
    }

    private val operations by lazy {
        BottomSheetDialog(requireContext()).apply {
            setContentView(operationsBinding.root)
        }
    }

    private val register: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result.data?.data?.let {
                grantUri(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
                it.asDocumentTreeModel(requireContext())
                    ?.let { file -> viewModel.loadState(path = file) }
            }
        }


    private val ioScope = CoroutineScope(IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener("operations") { key, bundle ->
            ioScope.launch {
                bundle.getParcelable<DocumentModel>("item")?.let {
                    when (bundle.getString("operation")) {
                        "Delete" -> {
                            subscribeRequest(request = deleteRequest(block = {
                                item(it)
                            }))
                        }
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FilemanagerFragmentBinding.inflate(inflater, container, false)
        onCreateViewInit(binding!!)
        return binding?.root
    }

    override fun onCreateViewInit(binding: FilemanagerFragmentBinding) {
        binding.run {
            list.adapter = adapter
            breadcrumbs.setListener(this@FileManagerFragment)
            refresh.setOnRefreshListener {
                viewModel.refreshState()
            }
            list.setHasFixedSize(true)
            refresh.setColorSchemeResources(R.color.colorAccent)
            refresh.setProgressBackgroundColorSchemeResource(R.color.bottomBarColor)
            requireActivity().onBackPressedDispatcher.addCallback(
                owner = viewLifecycleOwner,
                enabled = true
            ) {
                if (breadcrumbs.getItem()?.models?.size == 1) {
                    finish()
                } else {
                    safetyNavigateToParent()
                }
            }
        }
    }

    fun navigateToCreate() {
        findNavController().navigate(
            R.id.action_fileManagerFragment_to_fileManagerCreateFragment,
            Bundle().apply {
                putString("path", viewModel.currentPathFlow.value.getPath())
            })
    }

    fun refresh() = viewModel.refreshState()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val actBinding = ActivityMainBinding.bind(requireActivity().findViewById(R.id.root))

        findNavController().getLiveData<DocumentModel>(CREATE_KEY)?.observe(viewLifecycleOwner) {
            ioScope.launch {
                subscribeRequest(request = createRequest(block = {
                    item(it)
                }))
            }
        }

        findNavController().getLiveData<List<DocumentModel>>("CREATE_ARRAY")
            ?.observe(viewLifecycleOwner) {
                ioScope.launch {
                    subscribeRequest(request = createRequest(it.logEachName().toList()))
                }
            }

        binding?.run {
            progress.hide()

            list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0) {
                        actBinding.bar.performHide()
                    } else {
                        actBinding.bar.performShow()
                    }
                }
            })

            lifecycleScope.launch {
                combine(
                    viewModel.stateFlow,
                    viewModel.currentPathFlow,
                    viewModel.refreshFlow,
                    viewModel.loadingFlow
                ) { state, currentPath, isRef, isLoading ->
                    ScreenState(isLoading, isRef, currentPath, state)
                }.flowWithLifecycle(lifecycle = lifecycle, minActiveState = Lifecycle.State.STARTED)
                    .collect { (isLoading: Boolean, isRefreshing: Boolean, currentPath: DocumentModel, state: ViewModelState) ->
                        breadcrumbs.setItem(BreadcrumbItem.create(currentPath))
                        refresh.isRefreshing = isRefreshing
                        progress.toggle(isLoading)

                        when (state) {
                            is ListState -> {
                                if (state.list.size < 10) {
                                    actBinding.bar.performShow()
                                }
                                adapter.setData(state.list)
                            }
                        }
                    }
            }
        }
    }

    fun safetyNavigateToParent() {
        viewModel.currentPathFlow.value.getParentModel()?.let { navigateTo(it) }
    }

    override fun navigateTo(model: DocumentModel) {
        viewModel.updateState(path = model)
    }

    override fun copyPath(model: DocumentModel) {
        val board = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        board.setPrimaryClip(
            ClipData.newPlainText(
                "Path",
                model.getPath()
            )
        )
        "Copied!".anchoredSnackIt(fab)
    }

    override fun copyShortInfo(model: DocumentModel) {
        val board = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        board.setPrimaryClip(
            ClipData.newPlainText(
                "Short info ${model.getName()}",
                "Name: ${model.getName()} \nPath: ${model.getPath()} \nSize: ${model.getSize()}"
            )
        )
        "Copied!".anchoredSnackIt(fab)
    }

    override fun onClick(item: DocumentModel, view: View) {
        when (view) {
            is LinearLayoutCompat -> {
                if (item.isDirectory()) {
                    if (isAndroidR()) {
                        if (item.getName() == "data") {
                            register.launch(Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
                                putExtra(
                                    DocumentsContract.EXTRA_INITIAL_URI,
                                    Uri.parse("content://com.android.externalstorage.documents/document/primary%3AAndroid%2Fdata%2F")
                                )
                            })
                        }
                    }
                    navigateTo(item)
                } else {

                }
            }
            is AppCompatImageButton -> {
                operations.show()
                operationsBinding.operations.setNavigationItemSelectedListener {
                    when (it.title) {
                        "Delete" -> {
                            ioScope.launch {
                                subscribeRequest(request = deleteRequest(block = {
                                    item(item)
                                }))
                            }
                            operations.dismiss()
                            true
                        }

                        else -> false
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private suspend fun subscribeRequest(request: Request) {
        viewModel.request(request, operationCallback = object : OperationJobCallback() {
            override fun onGranted(operation: Operation, reason: Reason) {
                operation.priority
            }

            override fun onDenied(operation: Operation, reason: Reason) {
            }

        }, operationItemCallback = object : RenameModelJobCallback() {

            override fun onRenameSuccess(
                operation: Operation,
                oldModel: DocumentModel,
                destModel: DocumentModel,
                reason: Reason,
            ) {
                adapter.changeItem(oldModel, destModel)
            }

            override fun onRenameFailure(
                operation: Operation,
                oldModel: DocumentModel,
                destModel: DocumentModel,
                reason: Reason,
            ) {
                if (reason is TextualReason) {
                    reason.text.anchoredSnackIt(requireActivity().findViewById(R.id.fab))
                }
            }

            override fun onSuccess(
                operation: Operation,
                item: DocumentModel,
                reason: Reason,
            ) {
                adapter.onSuccessOperation(item = item, operation = operation)
            }

            override fun onFailure(
                operation: Operation,
                item: DocumentModel,
                reason: Reason,
            ) {
                if (reason is TextualReason) {
                    reason.text.anchoredSnackIt(requireActivity().findViewById(R.id.fab))
                }
                reason.logIt()
            }
        })
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        result.getParcelable<DocumentModel>("currentPath")?.let {
            ioScope.launch {
                subscribeRequest(request = createRequest(block = {
                    item(it)
                }))
            }
        }
    }

}