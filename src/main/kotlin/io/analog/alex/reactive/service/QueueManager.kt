package io.analog.alex.reactive.service

import io.analog.alex.reactive.models.InboundCall
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Service
import java.util.*

@Service
class QueueManager(
    private val redisson: RedissonClient
) {
    companion object {
        private const val QUEUE_NAME = "throttled-calls"
    }

    fun queueCall(call: InboundCall) {
        val throttledCalls = redisson.getQueue<InboundCall>(QUEUE_NAME)
        throttledCalls.add(call)
    }

    fun peekThrottledCalls(): Set<InboundCall> {
        val throttledCalls = redisson.getQueue<InboundCall>(QUEUE_NAME)
        return throttledCalls.readAll().toSet()
    }

    fun fetchThrottledCalls(): Queue<InboundCall> {
        return redisson.getQueue(QUEUE_NAME)
    }
}