package com.excu_fcd.filemanagerclient.mvvm.feature.worker.result

abstract class Result {

    companion object {
        fun success(): Success = Success()
        fun failure(): Failure = Failure()
        fun empty() = object : Result() {}
    }

}