package com.excu_fcd.core.extensions

import com.excu_fcd.core.data.model.DocumentModel
import com.excu_fcd.core.data.request.Request
import com.excu_fcd.core.data.request.operation.Operation
import com.excu_fcd.core.data.request.operation.operation
import com.excu_fcd.core.data.request.operation.type.CopyType
import com.excu_fcd.core.data.request.operation.type.CutType
import com.excu_fcd.core.data.request.priority.Priority
import com.excu_fcd.core.data.request.requestBuilder

// Fast Delete Request
fun deleteRequest(
    items: List<DocumentModel>,
    requestPriority: Priority = Priority.middle(),
    operationPriority: Priority = Priority.middle(),
): Request = fastRequest(items, requestPriority, operationPriority, Operation.Type.delete())

// Fast Delete Request
inline fun deleteRequest(
    block: MutableList<DocumentModel>.() -> Unit,
    requestPriority: Priority = Priority.middle(),
    operationPriority: Priority = Priority.middle(),
): Request = fastRequest(block = block,
    requestPriority,
    operationPriority = operationPriority,
    type = Operation.Type.delete())

// Fast Create Request
fun createRequest(
    items: List<DocumentModel>,
    requestPriority: Priority = Priority.middle(),
    operationPriority: Priority = Priority.middle(),
): Request = fastRequest(items, requestPriority, operationPriority, Operation.Type.create())

// Fast Create Request
inline fun createRequest(
    block: MutableList<DocumentModel>.() -> Unit,
    requestPriority: Priority = Priority.middle(),
    operationPriority: Priority = Priority.middle(),
): Request = fastRequest(block = block,
    requestPriority,
    operationPriority = operationPriority,
    type = Operation.Type.create())

// Fast Rename Request
fun renameRequest(
    items: List<DocumentModel>,
    requestPriority: Priority = Priority.middle(),
    operationPriority: Priority = Priority.middle(),
): Request = fastRequest(items, requestPriority, operationPriority, Operation.Type.rename())

// Fast Rename Request
inline fun renameRequest(
    block: MutableList<DocumentModel>.() -> Unit,
    requestPriority: Priority = Priority.middle(),
    operationPriority: Priority = Priority.middle(),
): Request = fastRequest(block = block,
    requestPriority,
    operationPriority = operationPriority,
    type = Operation.Type.rename())

// Fast Cut Request
fun cutRequest(
    items: List<DocumentModel>,
    requestPriority: Priority = Priority.middle(),
    operationPriority: Priority = Priority.middle(),
): Request = fastRequest(items, requestPriority, operationPriority, CutType)


// Fast Cut Request
inline fun cutRequest(
    block: MutableList<DocumentModel>.() -> Unit,
    requestPriority: Priority = Priority.middle(),
    operationPriority: Priority = Priority.middle(),
): Request = fastRequest(block = block,
    requestPriority,
    operationPriority = operationPriority,
    type = CutType)

// Fast Copy Request
fun copyRequest(
    items: List<DocumentModel>,
    requestPriority: Priority = Priority.middle(),
    operationPriority: Priority = Priority.middle(),
): Request = fastRequest(items, requestPriority, operationPriority, CopyType)

// Fast Copy Request
inline fun copyRequest(
    block: MutableList<DocumentModel>.() -> Unit,
    requestPriority: Priority = Priority.middle(),
    operationPriority: Priority = Priority.middle(),
): Request = fastRequest(block = block,
    requestPriority,
    operationPriority = operationPriority,
    type = CopyType)

// For smaller shit code!
fun fastRequest(
    items: List<Operation>,
    requestPriority: Priority = Priority.middle(),
): Request {
    return requestBuilder {
        requestName("Fast Request!")
        requestPriority(requestPriority)
        requestOperations {
            items(items = items)
        }
    }
}

// For smaller shit code!
inline fun fastRequest(
    block: MutableList<Operation>.() -> Unit,
    requestPriority: Priority = Priority.middle(),
): Request {
    return fastRequest(items = mutableListOf<Operation>().apply(block),
        requestPriority = requestPriority)
}

// For smaller shit code!
fun fastRequest(
    items: List<DocumentModel>,
    requestPriority: Priority = Priority.middle(),
    operationPriority: Priority = Priority.middle(),
    type: Operation.Type,
): Request {
    return requestBuilder {
        requestName("${type.javaClass} request")
        requestPriority(requestPriority)
        requestOperations {
            operation {
                data(items = items)
                priority(priority = operationPriority)
                type(type = type)
            }
        }
    }
}

// For smaller shit code!
inline fun fastRequest(
    block: MutableList<DocumentModel>.() -> Unit,
    requestPriority: Priority = Priority.middle(),
    operationPriority: Priority = Priority.middle(),
    type: Operation.Type,
): Request {
    return fastRequest(items = mutableListOf<DocumentModel>().apply(block),
        requestPriority = requestPriority,
        operationPriority = operationPriority,
        type = type)
}