package io.analog.alex.reactive.daemon

import io.analog.alex.reactive.models.InboundCall
import io.analog.alex.reactive.service.CallDispatcher
import io.analog.alex.reactive.service.QueueManager

import org.redisson.api.RedissonClient
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class Dequeuer(
    private val queueManager: QueueManager,
    private val callDispatcher: CallDispatcher
) {
    private val logger = LoggerFactory.getLogger(Dequeuer::class.java)

    @Scheduled(fixedDelay = 500)
    fun dequeue() {
        logger.debug("Scheduled task is processing")
        queueManager.fetchThrottledCalls().takeIf { it.isNotEmpty() }?.let { queue ->
            callDispatcher.processCall(
                queue.poll().also { logger.info("Dequeued and processed call: '$it'") }
            )
        } ?: logger.debug("Nothing to dequeue")
    }
}