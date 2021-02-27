package io.analog.alex.reactive.web

import io.analog.alex.reactive.models.InboundCall
import io.analog.alex.reactive.models.TwiMLResponse
import io.analog.alex.reactive.service.RateLimitedTelephony
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.time.Instant

@RestController
class ReactiveController(
    private val rateLimitedTelephony: RateLimitedTelephony
) {
    @GetMapping(value = ["/ping"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun sayHello() = Mono.just("Pong at ${Instant.now()}!")

    @PostMapping(
        value = ["/call/inbound"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun inboundCall(@RequestBody call: InboundCall): Mono<TwiMLResponse> {
        val twiML = rateLimitedTelephony.routeInboundCall(call)
        return Mono.just(twiML)
    }
}