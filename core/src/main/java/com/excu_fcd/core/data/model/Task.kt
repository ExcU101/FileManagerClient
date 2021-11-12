package com.excu_fcd.core.data.model

import android.os.Parcelable
import androidx.work.WorkInfo
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Task(
    val id: UUID,
    val name: String,
    val state: WorkInfo.State,
) : Parcelable {
}