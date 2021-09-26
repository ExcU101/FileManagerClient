package com.excu_fcd.filemanageclient.mvvm.data.local.source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert

@Dao
interface AbstractDao<I> {

    @Delete
    fun delete(item: I)

    @Insert
    fun insert(item: I)

}