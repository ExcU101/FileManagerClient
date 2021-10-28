package com.excu_fcd.core.data.model

import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow

class DocumentFlow(
    override val replayCache: List<DocumentModel>,
    override val value: DocumentModel,
) : StateFlow<DocumentModel> {

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<DocumentModel>) {
        collector.emit(replayCache.first())
    }
}