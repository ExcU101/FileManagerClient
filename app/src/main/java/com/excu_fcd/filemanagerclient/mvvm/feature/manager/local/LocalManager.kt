package com.excu_fcd.filemanagerclient.mvvm.feature.manager.local

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.os.EnvironmentCompat
import com.excu_fcd.filemanagerclient.mvvm.data.local.LocalUriModel
import com.excu_fcd.filemanagerclient.mvvm.data.request.Request
import com.excu_fcd.filemanagerclient.mvvm.feature.LocalEventPack
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.EmployerManager
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.FileNotificationManager
import com.excu_fcd.filemanagerclient.mvvm.feature.worker.local.CreateWorker
import com.excu_fcd.filemanagerclient.mvvm.feature.worker.local.DeleteWorker
import com.excu_fcd.filemanagerclient.mvvm.utils.asLocalUriModel
import java.io.File
import javax.inject.Inject

class LocalManager @Inject constructor(private val context: Context) :
    EmployerManager<LocalUriModel, LocalEventPack>(context = context) {

    private val notificationManager: FileNotificationManager =
        FileNotificationManager(context = context)

    companion object {
        const val RES: String = Manifest.permission.READ_EXTERNAL_STORAGE
        const val WES: String = Manifest.permission.WRITE_EXTERNAL_STORAGE

        @RequiresApi(Build.VERSION_CODES.R)
        val requestManageFiles = Intent().apply {
            action = ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
        }

        @RequiresApi(Build.VERSION_CODES.R)
        val MES = Manifest.permission.MANAGE_EXTERNAL_STORAGE

        val SDCARD: File = Environment.getExternalStorageDirectory().absoluteFile

        fun checkState(path: File): Boolean {
            return EnvironmentCompat.getStorageState(path) == Environment.MEDIA_MOUNTED
        }
    }

    private val workers = listOf(
        DeleteWorker(),
        CreateWorker()
    )

    @RequiresApi(Build.VERSION_CODES.R)
    fun requireSpecialPermission(): Boolean = !Environment.isExternalStorageManager()

    fun requirePermissions(): Boolean {
        return !(ContextCompat.checkSelfPermission(context,
            WES) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context,
            RES) == PackageManager.PERMISSION_GRANTED)
    }

    fun getExternalDirs() = context.getExternalFilesDir("")?.listFiles()?.map {
        it.asLocalUriModel()
    }

    fun getListFromPath(path: File): List<LocalUriModel> {
        if (!path.exists()) return emptyList()

        if (!requirePermissions()) {
            if (checkState(path = path) && path.isDirectory) {
                return path.listFiles()!!.map { LocalUriModel(uri = it.toUri()) }
            }
            return emptyList()
        }
        return emptyList()
    }

    fun getListFromPath(model: LocalUriModel): List<LocalUriModel> {
        return getListFromPath(model.getFile())
    }

    override suspend fun sendRequest(
        request: Request<LocalUriModel>,
        onResponse: (LocalEventPack) -> Unit,
    ) {
//        if (checkPermissions()) return
        for (worker in workers) {
            if (worker.confirm(request = request)) {
                worker.work(request = request, onResponse)
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