package com.hyecheon.learnractivespring.repository

import com.hyecheon.learnractivespring.document.*
import org.springframework.data.mongodb.repository.*
import reactor.core.publisher.*

interface ItemReactiveRepository : ReactiveMongoRepository<Item, String> {

	fun findByDescription(description: String): Mono<Item>

}