package com.excu_fcd.filemanagerclient.mvvm.feature

interface EventHandler<T> {

    suspend fun getEvent(startItem: T)

}