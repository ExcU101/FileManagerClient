package com.excu_fcd.core.data.model.common

import com.excu_fcd.core.data.model.Path

abstract class AbstractListPath<P : AbstractListPath<P>> : Path<P> {

    private val listPath: MutableList<P>
        get() = mutableListOf()

    fun getSegments() = listPath

    override fun getParent(): P {
        return resolve(subPath(0, getSegmentsCount() - 1))
    }

    override fun resolve(path: P): P {
        if (path.isAbsolute() || listPath.isEmpty()) return path
        return path
    }

    protected abstract fun createNewPath(isAbsolute: Boolean = true, segments: List<P>): P

    override fun subPath(startIndex: Int, endIndex: Int): P {
        return listPath.removeAt(index = endIndex)
    }

    override fun iterator(): Iterator<P> = listPath.iterator()

    abstract fun getSegmentsCount(): Int

}