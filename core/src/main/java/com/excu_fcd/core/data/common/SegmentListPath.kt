package com.excu_fcd.core.data.common

abstract class SegmentListPath<P : SegmentListPath<P>> : AbstractListPath<P>() {

    override fun getSegmentsCount(): Int {
        return getSegments().size
    }

}