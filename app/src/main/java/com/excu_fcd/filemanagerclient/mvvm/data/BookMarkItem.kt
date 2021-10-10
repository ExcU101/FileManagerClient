package com.excu_fcd.filemanagerclient.mvvm.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BookMarkItem(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "file_name")
    val name: String?,
    @ColumnInfo(name = "file_path")
    val path: String?,
)