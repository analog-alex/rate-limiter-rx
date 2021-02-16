package io.analog.alex.reactive.utilities.extensions

import io.analog.alex.reactive.models.InboundCall
import io.analog.alex.reactive.models.TwiMLResponse
import java.util.*

fun uuid() = UUID.randomUUID().toString().replace("-", "")
fun twiMLpass(call: InboundCall) = TwiMLResponse("Call was transferred to ${call.system}")
fun twiMLqueue(call: InboundCall) = TwiMLResponse("Play waiting music. Calls are throttled in for account ${call.accountId}")