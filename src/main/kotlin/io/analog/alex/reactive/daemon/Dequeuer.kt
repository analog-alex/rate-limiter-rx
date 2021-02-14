package io.analog.alex.reactive.daemon

import io.analog.alex.reactive.models.InboundCall

import org.redisson.api.RedissonClient
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class Dequeuer(
    private val redisson: RedissonClient
) {
    private val logger = LoggerFactory.getLogger(Dequeuer::class.java)

    @Scheduled(fixedDelay = 500)
    fun dequeu() {
        logger.info("task")
        val throttledCalls = redisson.getQueue<InboundCall>("throttled-calls")
            .takeIf { it.isNotEmpty() } ?: return

        val next = throttledCalls.poll()
        logger.info("Dequeue call $next")
    }
}