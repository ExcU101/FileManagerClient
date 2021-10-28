package com.excu_fcd.core.data.request.operation.reason

import com.excu_fcd.core.data.request.operation.OperationDataEmptyReason

interface Reason {

    companion object {
        fun empty() = EmptyReason

        fun text(text: String) = TextualReason(text = text)

        fun operationEmptyData() = OperationDataEmptyReason
    }

}