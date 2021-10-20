package com.excu_fcd.filemanagerclient.mvvm.data

import androidx.documentfile.provider.DocumentFile
import com.excu_fcd.filemanagerclient.mvvm.data.local.LocalUriModel
import com.excu_fcd.filemanagerclient.mvvm.utils.asLocalUriModel
import java.io.File

data class BreadcrumbItem(val models: List<LocalUriModel>, val selected: Int) {

    companion object {
        fun create(originalPath: LocalUriModel): BreadcrumbItem {
            val pair =
                createPathList(originalPath = originalPath)

            return BreadcrumbItem(models = pair.first, selected = pair.second)
        }

        fun create(originalPath: File): BreadcrumbItem {
            val pair =
                createPathList(originalPath = originalPath.asLocalUriModel())

            return BreadcrumbItem(models = pair.first, selected = pair.second)
        }

        private fun createPathList(originalPath: LocalUriModel): Pair<List<LocalUriModel>, Int> {
            var path = originalPath.getFile()
            val trip = mutableListOf<DocumentFile>()
            while (true) {
                trip.add(element = path)
                path = path.parentFile ?: break
            }
            trip.reverse()
            return Pair(first = trip.map { it.asLocalUriModel() }, second = trip.lastIndex)
        }
    }

}