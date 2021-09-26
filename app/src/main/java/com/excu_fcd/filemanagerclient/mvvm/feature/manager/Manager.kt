package com.excu_fcd.filemanagerclient.mvvm.feature.manager

import com.excu_fcd.filemanagerclient.mvvm.data.Nameable

interface Manager : Nameable {

    fun getTag(): String

}