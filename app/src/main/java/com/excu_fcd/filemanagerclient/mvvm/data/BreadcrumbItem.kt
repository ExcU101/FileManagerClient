package com.excu_fcd.filemanagerclient.mvvm.data

import com.excu_fcd.core.data.model.DocumentModel
import com.excu_fcd.core.extensions.asDocumentModel

data class BreadcrumbItem(val models: List<DocumentModel>, val selected: Int) {

    companion object {
        fun create(originalPath: DocumentModel): BreadcrumbItem {
            val pair =
                createPathList(originalPath = originalPath)

            return BreadcrumbItem(models = pair.first, selected = pair.second)
        }

        private fun createPathList(originalPath: DocumentModel): Pair<List<DocumentModel>, Int> {
            var path = originalPath
            val trip = mutableListOf<DocumentModel>()
            while (true) {
                trip.add(element = path)
                path = path.getParent()?.asDocumentModel() ?: break
            }
            trip.reverse()
            return Pair(first = trip, second = trip.lastIndex)
        }
    }

}