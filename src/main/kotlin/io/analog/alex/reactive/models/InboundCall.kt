package io.analog.alex.reactive.models

import java.io.Serializable

data class InboundCall(
    val to: String,
    val from: String,
    val system: System,
    val accountId: String
) : Serializable
