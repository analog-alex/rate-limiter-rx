package io.analog.alex.reactive.utilities.entities

import io.analog.alex.reactive.models.InboundCall
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("call")
data class ProcessedCall(
    @Id val id: String,
    @Indexed val accountId: String,
    val inboundCall: InboundCall,
    val at: Instant = Instant.now()
)
