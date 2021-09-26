package com.excu_fcd.filemanageclient.mvvm.feature.manager

import com.excu_fcd.filemanageclient.mvvm.data.Nameable

interface Manager : Nameable {

    fun getTag(): String

}