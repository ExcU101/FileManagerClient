package com.excu_fcd.core.data.common

import com.excu_fcd.core.data.Path

abstract class AbstractListPath<P : AbstractListPath<P>> : Path<P> {

    private val listPath: MutableList<P>
        get() = mutableListOf()

    fun getSegments() = listPath

    override fun getParent(): P {
        return resolve(subPath(0, getSegmentsCount() - 1))
    }

    override fun resolve(path: P): P {
        return path
    }

    override fun subPath(startIndex: Int, endIndex: Int): P {
        return listPath.removeAt(index = endIndex)
    }

    override fun iterator(): Iterator<P> = listPath.iterator()

    abstract fun getSegmentsCount(): Int

}