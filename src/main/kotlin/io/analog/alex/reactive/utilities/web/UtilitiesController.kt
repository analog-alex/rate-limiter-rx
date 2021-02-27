package io.analog.alex.reactive.utilities.web

import io.analog.alex.reactive.service.QueueManager
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("utilities")
class UtilitiesController(
    private val queueManager: QueueManager,
) {
    @GetMapping(value = ["/queue"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun peekCalls() = Flux.just(queueManager.peekThrottledCalls())
}