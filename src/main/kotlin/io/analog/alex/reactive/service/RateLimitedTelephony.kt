package io.analog.alex.reactive.service

import io.analog.alex.reactive.models.InboundCall
import io.analog.alex.reactive.models.TwiMLResponse
import io.analog.alex.reactive.utilities.extensions.twiMLpass
import io.analog.alex.reactive.utilities.extensions.twiMLqueue
import org.redisson.api.RateIntervalUnit
import org.redisson.api.RateType
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct

@Service
class RateLimitedTelephony(
    private val rateLimiter: RateLimiter,
    private val queueManager: QueueManager,
    private val callDispatcher: CallDispatcher
) {
    private val logger = LoggerFactory.getLogger(RateLimitedTelephony::class.java)

    fun routeInboundCall(call: InboundCall): TwiMLResponse {
        val goAhead = rateLimiter.tryAcquire()
        logger.info("Lock acquisition returned $goAhead for call $call")

        return when (goAhead) {
            true -> {
                logger.info("Dispatch call $call")
                callDispatcher.processCall(call)
                twiMLpass(call)
            }
            false -> {
                logger.info("Queue call $call")
                queueManager.queueCall(call)
                twiMLqueue(call)
            }
        }
    }
}