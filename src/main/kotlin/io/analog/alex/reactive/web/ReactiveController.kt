package io.analog.alex.reactive.web

import io.analog.alex.reactive.models.InboundCall
import io.analog.alex.reactive.service.RateLimitedTelephony
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Instant

@RestController
class ReactiveController(
    private val rateLimitedTelephony: RateLimitedTelephony
) {
    private val logger = LoggerFactory.getLogger(ReactiveController::class.java)

    @GetMapping(value = ["/ping"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun sayHello(): ResponseEntity<*> {
        return ResponseEntity.ok().body(Mono.just("Pong at ${Instant.now()}!"));
    }

    @PostMapping(
        value = ["/call/inbound"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun inboundCall(@RequestBody call: InboundCall): ResponseEntity<*> {
        logger.info("Inbound call received: $call")
        val twiML = rateLimitedTelephony.routeInboundCall(call)
        return ResponseEntity.ok().body(Mono.just(twiML));
    }
}