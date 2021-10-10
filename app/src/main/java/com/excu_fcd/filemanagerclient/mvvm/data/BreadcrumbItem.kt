package com.excu_fcd.filemanagerclient.mvvm.data

import com.excu_fcd.filemanagerclient.mvvm.data.local.LocalUriModel
import com.excu_fcd.filemanagerclient.mvvm.utils.asLocalUriModel
import java.io.File

data class BreadcrumbItem(val models: List<LocalUriModel>, val selected: Int) {

    companion object {
        fun create(originalPath: LocalUriModel): BreadcrumbItem {
            return BreadcrumbItem(createPathList(originalPath = originalPath), selected = 1)
        }

        fun create(originalPath: File): BreadcrumbItem {
            return BreadcrumbItem(createPathList(originalPath = originalPath.asLocalUriModel()),
                selected = 1)
        }

        private fun createPathList(originalPath: LocalUriModel): List<LocalUriModel> {
            var path: File = originalPath.getFile()
            val trip = mutableListOf<File>()
            while (true) {
                trip.add(element = path)
                path = path.parentFile ?: break
            }
            trip.reverse()
            return trip.map { it.asLocalUriModel() }
        }
    }

}