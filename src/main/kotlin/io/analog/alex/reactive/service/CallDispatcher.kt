package io.analog.alex.reactive.service

import io.analog.alex.reactive.models.InboundCall
import io.analog.alex.reactive.utilities.entities.ProcessedCall
import io.analog.alex.reactive.utilities.extensions.uuid
import io.analog.alex.reactive.utilities.repositories.ProcessedCallsRepository
import org.springframework.stereotype.Service

@Service
class CallDispatcher(
    private val processedCallsRepository: ProcessedCallsRepository
) {
    fun processCall(call: InboundCall) = processedCallsRepository.save(
        ProcessedCall(
            id = uuid(),
            accountId = call.accountId,
            inboundCall = call
        )
    )
}
