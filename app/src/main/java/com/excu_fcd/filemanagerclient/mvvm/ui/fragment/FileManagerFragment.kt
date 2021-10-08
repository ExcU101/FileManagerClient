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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.excu_fcd.filemanagerclient.R
import com.excu_fcd.filemanagerclient.databinding.CreateNowFragmentBinding
import com.excu_fcd.filemanagerclient.databinding.FilemanagerFragmentBinding
import com.excu_fcd.filemanagerclient.mvvm.data.local.LocalUriModel
import com.excu_fcd.filemanagerclient.mvvm.data.request.Request
import com.excu_fcd.filemanagerclient.mvvm.data.request.type.DeleteOperationType
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.local.LocalManager.Companion.SDCARD
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.local.state.SortedListState
import com.excu_fcd.filemanagerclient.mvvm.ui.adapter.LocalAdapter
import com.excu_fcd.filemanagerclient.mvvm.ui.adapter.listener.OnViewClickListener
import com.excu_fcd.filemanagerclient.mvvm.ui.adapter.swipe.LocalSwipeHelper
import com.excu_fcd.filemanagerclient.mvvm.utils.*
import com.excu_fcd.filemanagerclient.mvvm.viewmodel.LocalViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FileManagerFragment : Fragment(), OnViewClickListener<LocalUriModel> {

    private var binding: FilemanagerFragmentBinding? = null
    private var createAlertBinding: CreateNowFragmentBinding? = null

    private val viewModel: LocalViewModel by hiltNavGraphViewModels(R.id.app_navigation)
    private val adapter = LocalAdapter().apply {
        listener = this@FileManagerFragment
    }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            viewModel.update()
        }

    private val ioScope = CoroutineScope(IO)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FilemanagerFragmentBinding.inflate(layoutInflater, container, false)
        createAlertBinding = CreateNowFragmentBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            with(binding!!) {
                combine(viewModel.refreshState,
                    viewModel.data) { isRef, state ->
                    Pair(isRef, state)
                }.flowWithLifecycle(lifecycle = lifecycle,
                    Lifecycle.State.STARTED).collect { (isRef, state) ->

                    refresh.isRefreshing = isRef
                    refresh.setOnRefreshListener {
                        viewModel.refresh(SDCARD.asLocalUriModel())
                    }
                    when (state) {
                        is SortedListState -> {
                            list.animate().alpha(if (isRef) 0F else 1F).start()
                            adapter.setData(state.list)
                            list.adapter = adapter
                            list.touchHelper(callback = LocalSwipeHelper(adapter = adapter))
                            bar.setOnMenuItemClickListener {
                                when (it.itemId) {
                                    R.id.refresh -> {
                                        viewModel.refresh(SDCARD.asLocalUriModel())
                                        true
                                    }
                                    else -> false
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onClick(item: LocalUriModel, view: View) {
        when (view) {
            is LinearLayoutCompat -> CoroutineScope(Dispatchers.Default).launch {
                "Linear".snackIt(view = view)
            }
            is AppCompatImageButton -> {
                view.popup(items = sortedList {
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