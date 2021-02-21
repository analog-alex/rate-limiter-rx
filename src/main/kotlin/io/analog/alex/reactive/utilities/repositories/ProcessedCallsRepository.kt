package io.analog.alex.reactive.utilities.repositories

import io.analog.alex.reactive.utilities.entities.ProcessedCall
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface ProcessedCallsRepository : ReactiveMongoRepository<ProcessedCall, String> {
    fun findByAccountId(accountId: String): Flux<ProcessedCall>
}