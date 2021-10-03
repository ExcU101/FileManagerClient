package com.excu_fcd.filemanagerclient.mvvm.feature

interface EventHandle<T> {

    suspend fun getEvent(startItem: T)

}