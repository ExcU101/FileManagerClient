package com.excu_fcd.filemanagerclient.mvvm.feature.manager.local

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.os.EnvironmentCompat
import com.excu_fcd.filemanagerclient.mvvm.data.local.LocalUriModel
import com.excu_fcd.filemanagerclient.mvvm.data.request.Request
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.EmployerManager
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.FileNotificationManager
import com.excu_fcd.filemanagerclient.mvvm.feature.worker.Worker
import com.excu_fcd.filemanagerclient.mvvm.feature.worker.local.DeleteWorker
import com.excu_fcd.filemanagerclient.mvvm.feature.worker.result.Result
import java.io.File
import javax.inject.Inject

class LocalManager @Inject constructor(private val context: Context) :
    EmployerManager<LocalUriModel>(context = context) {

    private val notificationManager: FileNotificationManager =
        FileNotificationManager(context = context)

    companion object {
        const val RES: String = Manifest.permission.READ_EXTERNAL_STORAGE
        const val WES: String = Manifest.permission.WRITE_EXTERNAL_STORAGE

        @RequiresApi(Build.VERSION_CODES.R)
        val MES = Manifest.permission.MANAGE_EXTERNAL_STORAGE

        val SDCARD: File = Environment.getExternalStorageDirectory()

        fun checkState(path: File): Boolean {
            return EnvironmentCompat.getStorageState(path) == Environment.MEDIA_MOUNTED
        }
    }

    private val workers = listOf<Worker<LocalUriModel>>(
        DeleteWorker()
    )

    fun checkPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else ContextCompat.checkSelfPermission(context,
            WES) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context,
            RES) == PackageManager.PERMISSION_GRANTED
    }

    fun getListFromPath(path: File): List<LocalUriModel> {
        if (!path.exists()) return emptyList()

        if (checkPermissions()) {
            if (checkState(path = path) && path.isDirectory) {
                return path.listFiles()!!.map { LocalUriModel(uri = it.toUri()) }
            }
            return emptyList()
        }
        return emptyList()
    }

    override suspend fun sendRequest(
        request: Request<LocalUriModel>,
        onResponse: (result: Result) -> Unit,
    ) {
        if (!checkPermissions() && !checkPermissions()) return

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            if (!checkPermission(MES)) {
//                for (worker in workers) {
//                    if (worker.confirm(request = request)) {
//                        onResponse(worker.work(request = request))
//                    }
//                }
//            }
//        }
        for (worker in workers) {
            if (worker.confirm(request = request)) {
                worker.work(request = request, onResponse = onResponse)
            }
        }
    }

    override fun getTag(): String {
        return "LOCAL_PROVIDER_TAG"
    }

    override fun getName(): String {
        return "Local provider"
    }

}