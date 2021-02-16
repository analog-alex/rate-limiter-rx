package io.analog.alex.reactive.utilities.repositories

import io.analog.alex.reactive.utilities.entities.ProcessedCall
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ProcessedCallsRepository : MongoRepository<ProcessedCall, String> {
    fun findByAccountId(accountId: String): List<ProcessedCall>
}