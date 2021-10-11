package com.excu_fcd.filemanagerclient.mvvm.data.local.source.dao

import androidx.room.Query
import com.excu_fcd.filemanagerclient.mvvm.data.BookMarkItem
import com.excu_fcd.filemanagerclient.mvvm.utils.localBookmark
import kotlinx.coroutines.flow.Flow

interface BookmarkDao : AbstractDao<BookMarkItem> {

    @Query("SELECT * FROM $localBookmark")
    fun getData(): Flow<List<BookMarkItem>>

}