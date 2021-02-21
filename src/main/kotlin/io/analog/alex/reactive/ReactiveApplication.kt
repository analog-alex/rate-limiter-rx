package io.analog.alex.reactive

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@EnableReactiveMongoRepositories
class ReactiveApplication

fun main(args: Array<String>) {
	runApplication<ReactiveApplication>(*args)
}
