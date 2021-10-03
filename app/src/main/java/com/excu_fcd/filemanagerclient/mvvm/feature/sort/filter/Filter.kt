package com.excu_fcd.filemanagerclient.mvvm.feature.sort.filter

interface Filter {

    companion object {
        fun empty() = object : Filter {}
    }

}