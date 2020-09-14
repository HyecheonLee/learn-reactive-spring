package com.hyecheon.learnractivespring.handler

import org.springframework.http.*
import org.springframework.stereotype.*
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.*

@Component
class SampleHandlerFunction {

	fun flux(serverRequest: ServerRequest) = run {
		ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(Flux.just(1, 2, 3, 4).log(), Int::class.java)
	}

	fun mono(serverRequest: ServerRequest) = run {
		ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(1).log(), Int::class.java)
	}
}