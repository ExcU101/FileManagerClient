package com.excu_fcd.core.provider

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.SavedStateHandle
import androidx.work.WorkManager
import androidx.work.WorkQuery
import androidx.work.WorkRequest
import com.excu_fcd.core.data.model.Document
import com.excu_fcd.core.data.model.DocumentModel
import com.excu_fcd.core.data.request.Request
import com.excu_fcd.core.extensions.asDocumentFile
import com.excu_fcd.core.extensions.asDocumentModel
import com.excu_fcd.core.extensions.isGranted
import com.excu_fcd.core.extensions.logIt
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

    private val tasker = WorkManager.getInstance(context)
    private val resolver = getContentResolver()

    fun getTasks(list: List<String>) =
        tasker.getWorkInfosLiveData(WorkQuery.Builder.fromUniqueWorkNames(list).build())


    fun <R : WorkRequest> pullTaskRequest(request: R) = tasker.enqueue(request)

    fun query(volumeName: String) {
        val uri = MediaStore.Files.getContentUri(volumeName)

        val selection = (MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_NONE)

        resolver.query(uri, null, selection, null, null)?.use {
            val id = it.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
            val name = it.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
            val mimeType = it.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE)
            val size = it.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE)
            val path = it.getColumnIndexOrThrow(MediaStore.MediaColumns.RELATIVE_PATH)

            while (it.moveToNext()) {
                Document(name = it.getString(name).logIt())
            }
        }
    }

    private val jobs = mutableListOf(
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
        return if (checkPerms()) requireNotNull(path.list()) else listOf()
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

    fun <T : Job> addJob(job: T) {
        jobs.add(job)
    }

    fun <T : Job> removeJob(job: T) {
        when (job) {
            is CreateJob -> {
                throw UnsupportedOperationException("Cant remove this job (${job.javaClass})")
            }

            is DeleteJob -> {
                throw UnsupportedOperationException("Cant remove this job (${job.javaClass})")
            }

            is RenameJob -> {
                throw UnsupportedOperationException("Cant remove this job (${job.javaClass})")
            }
        }
        jobs.remove(job)
    }

    fun getContext() = context

    fun getContentResolver() = getContext().contentResolver

    override suspend fun pullRequest(
        request: Request,
        operationCallback: OperationJobCallback?,
        itemOperationCallback: ModelJobCallback?,
    ) {
        for (operation in request.getOperationData()) {
            for (job in jobs) {
                job.doWork(
                    operation = operation,
                    operationCallback = operationCallback,
                    itemOperationCallback = itemOperationCallback
                )
            }
        }
    }

}