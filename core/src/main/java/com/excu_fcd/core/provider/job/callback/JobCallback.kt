package com.excu_fcd.core.provider.job.callback

import com.excu_fcd.core.extensions.logIt

abstract class JobCallback {

    fun onSuccess() {
        logIt().onSuccess()
    }

    fun onFailure() {}

}