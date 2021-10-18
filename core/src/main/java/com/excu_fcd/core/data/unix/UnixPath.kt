package com.excu_fcd.core.data.unix

import android.net.Uri
import com.excu_fcd.core.data.FileSystem
import com.excu_fcd.core.data.Path
import com.excu_fcd.core.data.common.SegmentListPath
import kotlinx.parcelize.Parcelize


// ETO PIZDEC BLYAT YA ZAEBALSA

@Parcelize
class UnixPath : SegmentListPath<UnixPath>() {

    override fun getPathSystem(): FileSystem<UnixPath> {
        return object : FileSystem<UnixPath> {
            override fun getName(): String {
                return "UNIX"
            }

            override fun get(index: Int): UnixPath {
                return UnixPath()
            }
        }
    }

    override fun getPartName(): String {
        return getPathSystem().getName()
    }

    override fun getName(index: Int): UnixPath {
        return getPathSystem().get(index = index)
    }


    override fun toUri(): Uri {
        return Uri.fromParts("unix", this.toString(), null)
    }

    override fun endsWith(other: Path<UnixPath>): Boolean {
        return false
    }

    override fun endsWith(other: String): Boolean {
        return false
    }
}