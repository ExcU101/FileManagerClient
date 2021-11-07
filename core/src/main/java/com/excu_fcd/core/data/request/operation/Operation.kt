package com.excu_fcd.core.data.request.operation

import android.os.Parcelable
import com.excu_fcd.core.data.model.DocumentModel
import com.excu_fcd.core.data.request.operation.Operation.Type.Companion.empty
import com.excu_fcd.core.data.request.operation.reason.Reason
import com.excu_fcd.core.data.request.operation.type.*
import com.excu_fcd.core.data.request.priority.Priority
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Operation(
    val data: List<DocumentModel> = listOf(),
    val priority: @RawValue Priority = Priority.empty(),
    val type: Type = Type.empty(),
) : Parcelable {
    interface Type : Parcelable {
        companion object {
            fun empty() = EmptyType

            fun delete() = DeleteType

            fun create() = CreateType

            fun rename() = RenameType

            //Copy + Delete
            fun cut() = CutType

            //Recreate
            fun copy() = CopyType

            fun convert() = ConvertType
        }
    }
}

class OperationBuilder {
    private var operationPriority = Priority.empty()
    private val data: MutableList<DocumentModel> = mutableListOf()
    private var operationType: Operation.Type = empty()

    fun data(block: MutableList<DocumentModel>.() -> Unit) {
        data(items = mutableListOf<DocumentModel>().apply(block))
    }

    fun data(items: List<DocumentModel>) {
        data.addAll(items)
    }

    fun priority(priority: Priority) {
        operationPriority = priority
    }

    fun type(type: Operation.Type) {
        operationType = type
    }

    fun build(): Operation =
        Operation(data = data, priority = operationPriority, type = operationType)
}

object OperationDataEmptyReason : Reason

inline fun MutableList<Operation>.operation(block: OperationBuilder.() -> Unit) {
    add(OperationBuilder().apply(block).build())
}