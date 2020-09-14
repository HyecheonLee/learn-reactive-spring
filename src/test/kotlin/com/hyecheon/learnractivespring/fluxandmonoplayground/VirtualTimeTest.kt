package com.hyecheon.learnractivespring.fluxandmonoplayground

import org.junit.jupiter.api.*
import reactor.core.publisher.*
import reactor.test.*
import reactor.test.scheduler.*
import java.time.*

class VirtualTimeTest {

	@Test
	internal fun testingWithoutVirtualTime() {
		val longFlux = Flux.interval(Duration.ofSeconds(1)).take(3)
		StepVerifier.create(longFlux.log())
				.expectSubscription()
				.expectNext(0L, 1L, 2L)
				.verifyComplete()
	}


	@Test
	internal fun testingWithVirtualTime() {
		VirtualTimeScheduler.getOrSet()
		val longFlux = Flux.interval(Duration.ofSeconds(1)).take(3)

		StepVerifier.withVirtualTime { longFlux.log() }
				.expectSubscription()
				.thenAwait(Duration.ofSeconds(3))
				.expectNext(0L, 1L, 2L)
				.verifyComplete()
	}
}