package com.excu_fcd.filemanagerclient.mvvm.viewmodel.state

interface ViewModelState {

    companion object {
        fun empty(): ViewModelState = object : ViewModelState {

        }

        fun loading() = LoadingState

        fun refresh(isRefreshing: Boolean = false) = RefreshState(isRefreshing = isRefreshing)
    }

}