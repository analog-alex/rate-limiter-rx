package io.analog.alex.reactive.service

import io.analog.alex.reactive.models.InboundCall
import io.analog.alex.reactive.models.TwiMLResponse
import org.redisson.api.RateIntervalUnit
import org.redisson.api.RateType
import org.redisson.api.RedissonClient
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct

@Service
class RateLimitedTelephony(
    @Value("\${rate.limit}") private val limit: Long,
    @Value("\${rate.period}") private val period: Long,
    @Value("\${rate.wait}") private val wait: Long,
    private val redisson: RedissonClient
) {
    private val logger = LoggerFactory.getLogger(RateLimitedTelephony::class.java)

    companion object {
        private const val RATE_LIMITER_NAME = "rate-limiter-inbounds"
    }

    @PostConstruct
    private fun initRateLimiter() {
        logger.info("Setting up rate limiter with name '$RATE_LIMITER_NAME'")
        val rateLimiter = redisson.getRateLimiter(RATE_LIMITER_NAME)
        rateLimiter.setRate(RateType.OVERALL, limit, period, RateIntervalUnit.SECONDS)
    }

    fun routeInboundCall(call: InboundCall): TwiMLResponse {
        val rateLimiter = redisson.getRateLimiter(RATE_LIMITER_NAME)
        val goAhead = rateLimiter.tryAcquire(1, wait, TimeUnit.SECONDS)

        return if (goAhead) {
            TwiMLResponse("Call was transferred to ${call.system}")
        } else {
            val throttledCalls = redisson.getQueue<InboundCall>("throttled-calls")
            throttledCalls.add(call)
            TwiMLResponse("Play waiting music. Calls are throttled in for account ${call.accountId}")
        }
    }

    fun peekThrottledCalls(): Set<InboundCall> {
        val throttledCalls = redisson.getQueue<InboundCall>("throttled-calls")
        return throttledCalls.readAll().toSet()
    }
}