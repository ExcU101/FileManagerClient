package com.excu_fcd.filemanagerclient.mvvm.data.local.source.base

import androidx.room.Database
import com.excu_fcd.filemanagerclient.mvvm.data.BookMarkItem

@Database(entities = [BookMarkItem::class], version = 1)
abstract class LocalBookmarkDatabase : AbstractBase() {
}