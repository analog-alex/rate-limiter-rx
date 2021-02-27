package io.analog.alex.reactive.service

import com.fasterxml.jackson.databind.ObjectMapper
import io.analog.alex.reactive.models.InboundCall
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

@Service
class CallDispatcher(
    @Value("\${base.system.url}") baseUrl: String
) {
    private val webClient: WebClient = WebClient.builder()
        .baseUrl(baseUrl)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build()

    private val objectMapper = ObjectMapper()

    fun processCall(call: InboundCall) = webClient.post()
        .uri("/api/v1/entity")
        .body(Mono.just(objectMapper.writeValueAsString(call)), String::class.java)
        .retrieve()
        .bodyToMono<String>()
        .then()
        .subscribe()
}
