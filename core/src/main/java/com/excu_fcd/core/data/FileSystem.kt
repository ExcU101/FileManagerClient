package com.excu_fcd.core.data

interface FileSystem<P : Path<P>> {

    fun getName(): String

    fun get(index: Int): P

}