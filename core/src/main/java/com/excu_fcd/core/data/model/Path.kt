package com.excu_fcd.core.data.model

import android.net.Uri
import android.os.Parcelable
import com.excu_fcd.core.data.model.common.AbstractListPath

interface Path<P : Path<P>> : Comparable<P>, Iterable<P>, Parcelable {

    fun getPathSystem(): FileSystem<P>

    fun getPartName(): String

    fun getName(index: Int): P

    fun getParent(): P

    fun toUri(): Uri

    fun resolve(path: P): P

    fun subPath(startIndex: Int, endIndex: Int): P

    fun endsWith(other: Path<P>): Boolean

    fun isAbsolute(): Boolean = false

    fun endsWith(other: String): Boolean

    override fun compareTo(other: P): Int {
        return compareBy<P> {
            it.getParent()
        }.compare(this as P, other)
    }
}

fun <P : AbstractListPath<P>> AbstractListPath<P>.addPath(path: P) {
    getSegments().add(path)
}

fun <P : AbstractListPath<P>> AbstractListPath<P>.removePath(path: P) {
    getSegments().remove(path)
}

fun <P : AbstractListPath<P>> AbstractListPath<P>.last() = getSegments().last()

fun <P : AbstractListPath<P>> AbstractListPath<P>.removeFirst() {
    getSegments().removeFirst()
}

fun <P : AbstractListPath<P>> AbstractListPath<P>.removeLast() {
    getSegments().removeLast()
}