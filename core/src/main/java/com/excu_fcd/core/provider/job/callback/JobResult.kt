package com.excu_fcd.core.provider.job.callback

abstract class JobResult {

    companion object {
        fun success() = object : JobResult() {

        }

        fun failure() = object : JobResult() {

        }
    }

}