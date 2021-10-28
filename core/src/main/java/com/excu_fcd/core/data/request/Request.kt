package com.excu_fcd.core.data.request

import com.excu_fcd.core.data.request.operation.Operation
import com.excu_fcd.core.data.request.priority.Priority
import com.excu_fcd.core.data.request.priority.Priority.Companion.empty
import com.excu_fcd.core.extensions.requestBuilderName
import com.excu_fcd.core.extensions.requestBuilderPriority

data class Request(

    // Simple request
    val requestName: String,

    // We make stack of operations for different tasks
    // Example: Planned create or delete in time task
    private val operations: List<Operation>,

    // How fast we need make this request (Hierarchy)
    // Example:
    // request1: Low priority
    // request2: High priority
    // Start pull request2... (because request2 has higher priority than request1 but request1 is higher in a stack of requests
    val priority: Priority,
) {
    fun getOperationData(): List<Operation> = operations

    @Marker
    object Builder {
        private val operations: MutableList<Operation> = mutableListOf()
        private var requestName = requestBuilderName
        private var requestPriority = requestBuilderPriority

        fun requestOperations(block: MutableList<Operation>.() -> Unit) {
            operations.addAll(mutableListOf<Operation>().apply(block))
        }

        fun requestName(name: String) {
            requestName = name
        }

        fun requestPriority(priority: Priority) {
            if (priority == empty()) throw Exception("Priority is empty")
            requestPriority = priority
        }

        fun build(): Request {
            if (requestPriority == empty()) throw Exception("Priority is empty")
            return Request(
                requestName = requestName,
                operations = operations,
                priority = requestPriority
            )
        }
    }

    @DslMarker
    private annotation class Marker
}

inline fun requestBuilder(block: Request.Builder.() -> Unit): Request {
    return Request.Builder.apply(block).build()
}