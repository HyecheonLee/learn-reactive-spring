package com.hyecheon.learnractivespring.controller

import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.*
import java.time.*

@RestController
class FluxAndMonoController {

	@GetMapping("/flux")
	fun returnFlux() = run {
		Flux.just(1, 2, 3, 4)
				.delayElements(Duration.ofSeconds(1))
				.log()
	}

	@GetMapping("/fluxstream", produces = [MediaType.APPLICATION_STREAM_JSON_VALUE])
	fun returnFluxStream() = run {
		Flux.interval(Duration.ofSeconds(1)).log()
	}

	@GetMapping("/mono")
	fun returnMono() = run {
		Mono.just(1)
				.log()
	}
}