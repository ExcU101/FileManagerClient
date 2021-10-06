package com.excu_fcd.filemanagerclient.mvvm.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import com.excu_fcd.filemanagerclient.R
import com.excu_fcd.filemanagerclient.databinding.FilemanagerFragmentBinding
import com.excu_fcd.filemanagerclient.mvvm.data.local.LocalUriModel
import com.excu_fcd.filemanagerclient.mvvm.data.request.Request
import com.excu_fcd.filemanagerclient.mvvm.data.request.type.DeleteOperationType
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.local.RequirePermissionState
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.local.RequireSpecialPermissionState
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.local.SortedListState
import com.excu_fcd.filemanagerclient.mvvm.feature.worker.result.Success
import com.excu_fcd.filemanagerclient.mvvm.ui.adapter.LocalAdapter
import com.excu_fcd.filemanagerclient.mvvm.ui.adapter.listener.OnViewClickListener
import com.excu_fcd.filemanagerclient.mvvm.utils.*
import com.excu_fcd.filemanagerclient.mvvm.viewmodel.LocalViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FileManagerFragment : Fragment(), OnViewClickListener<LocalUriModel> {

    private var binding: FilemanagerFragmentBinding? = null

    private val viewModel: LocalViewModel by hiltNavGraphViewModels(R.id.app_navigation)
    private val adapter = LocalAdapter().apply {
        listener = this@FileManagerFragment
    }

    private val ioScope = CoroutineScope(IO)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FilemanagerFragmentBinding.inflate(layoutInflater, null, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenStarted {
            viewModel.stateFlow.collect { model ->
                when (model) {
                    is RequireSpecialPermissionState -> {
                        val intent = Intent().apply {
                            action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                            data = Uri.fromParts("package", requireContext().packageName, null)
                        }
                        requireContext().startActivity(intent)
                        "Need special perm".toastIt(requireContext())
                    }

                    is RequirePermissionState -> {
                        "Need perms".toastIt(requireContext())
                    }

                    is SortedListState -> {
                        adapter.setData(newData = model.list)
                        binding!!.list.adapter = adapter
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
                                    if (result is Success) {
                                        MainScope().launch {
                                            adapter.removeItem(item = item)
                                            request.getProgress().logIt()
                                            request.getStatus().javaClass.simpleName.logIt()
                                        }
                                    }
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