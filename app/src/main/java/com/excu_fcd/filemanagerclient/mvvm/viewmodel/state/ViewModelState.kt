package com.excu_fcd.filemanagerclient.mvvm.viewmodel.state

interface ViewModelState {

    companion object {
        fun empty(): ViewModelState = object : ViewModelState {

        }
    }

}