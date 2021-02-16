package io.analog.alex.reactive.utilities.web

import io.analog.alex.reactive.service.QueueManager
import io.analog.alex.reactive.utilities.repositories.ProcessedCallsRepository
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("utilities")
class UtilitiesController(
    private val queueManager: QueueManager,
    private val processedCallsRepository: ProcessedCallsRepository
) {

    @GetMapping(value = ["/queue"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun peekCalls(): ResponseEntity<*> {
        return ResponseEntity.ok().body(Flux.just(queueManager.peekThrottledCalls()));
    }

    @GetMapping(value = ["/calls"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun processed(@RequestParam(required = false) accountId: String?): ResponseEntity<*> {
        val calls = accountId?.let {
            processedCallsRepository.findByAccountId(it)
        } ?: processedCallsRepository.findAll()

        return ResponseEntity.ok().body(Flux.just(calls));
    }
}