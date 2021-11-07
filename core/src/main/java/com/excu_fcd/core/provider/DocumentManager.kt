package com.excu_fcd.core.provider

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.SavedStateHandle
import com.excu_fcd.core.data.model.DocumentModel
import com.excu_fcd.core.data.request.Request
import com.excu_fcd.core.extensions.asDocumentFile
import com.excu_fcd.core.extensions.asDocumentModel
import com.excu_fcd.core.extensions.isGranted
import com.excu_fcd.core.provider.job.CreateJob
import com.excu_fcd.core.provider.job.DeleteJob
import com.excu_fcd.core.provider.job.Job
import com.excu_fcd.core.provider.job.RenameJob
import com.excu_fcd.core.provider.job.callback.ModelJobCallback
import com.excu_fcd.core.provider.job.callback.OperationJobCallback
import javax.inject.Inject

class DocumentManager @Inject constructor(
    private val context: Context,
) : ContextManager(context = context), RequestedManager {


    private var jobs = mutableListOf(
        DeleteJob,
        CreateJob,
        RenameJob
    )

    private var needToRequestSpecialPermission = false
    private var needToRequestPermission = false

    private var state: SavedStateHandle? = null

    private var currentPath: DocumentModel? = state?.get(CURRENT_PATH_STATE)

    companion object {
        private const val REGISTRY_KEY_PATH = "PATH"
        const val CURRENT_PATH_STATE = "CURRENT_PATH_STATE"
    }

    private fun checkPerms(permission: String = Manifest.permission.READ_EXTERNAL_STORAGE): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            isGranted(context = context, permission = permission)
        }
    }

    fun getCurrentPathPath() = currentPath?.getPath()

    fun getPath(uri: Uri) = uri.asDocumentFile(context)?.asDocumentModel()?.getName()

    fun getListFromPath(path: DocumentModel): List<DocumentModel> {
        return if (checkPerms()) requireNotNull(path.list()) else throw IllegalArgumentException("Perms not granted")
    }

    fun loadFromCurrentPath(): List<DocumentModel>? {
        return currentPath?.list()
    }

    fun getCurrentPathFromState(value: DocumentModel) {
        state?.set(CURRENT_PATH_STATE, value)
        currentPath = value
    }

    fun getCurrentPathFromState(state: SavedStateHandle): DocumentModel? {
        this.state = state
        currentPath = state.get<DocumentModel>(CURRENT_PATH_STATE)
        return currentPath
    }

    fun requestForCurrentPath(registry: ActivityResultRegistry, uri: Uri) {
        registry.register(REGISTRY_KEY_PATH, ActivityResultContracts.OpenDocumentTree()) {
            currentPath = it.asDocumentFile(context)?.asDocumentModel()
        }.launch(uri)
    }

    override suspend fun pullRequest(
        request: Request,
        operationCallback: OperationJobCallback?,
        itemOperationCallback: ModelJobCallback?,
    ) {
        for (operation in request.getOperationData()) {
            for (job in jobs) {
                job.doWork(operation = operation, operationCallback, itemOperationCallback)
            }
        }
    }

}