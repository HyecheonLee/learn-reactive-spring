package com.hyecheon.learnractivespring.fluxandmonoplayground

import org.junit.jupiter.api.*
import reactor.core.publisher.*
import reactor.test.*
import java.lang.Thread.*
import java.time.*

class FluxAndMonoWithTImeTest {
	@Test
	internal fun infiniteSequence() {
		val infiniteFlux = Flux.interval(Duration.ofMillis(200)) // start from 0 -> .... N
				.log()

		infiniteFlux.subscribe { e ->
			println("value is $e")
		}

		sleep(3000)
	}

	@Test
	internal fun infiniteSequenceTest() {

		val finiteFlux = Flux.interval(Duration.ofMillis(100)) // start from 0 -> .... N
				.take(3)
				.log()

		StepVerifier.create(finiteFlux)
				.expectSubscription()
				.expectNext(0L, 1L, 2L)
				.verifyComplete()

	}

	@Test
	internal fun infiniteSequenceMap() {

		val finiteFlux = Flux.interval(Duration.ofMillis(100)) // start from 0 -> .... N
				.map { it.toInt() }
				.take(3)
				.log()

		StepVerifier.create(finiteFlux)
				.expectSubscription()
				.expectNext(0, 1, 2)
				.verifyComplete()

	}

	@Test
	internal fun infiniteSequenceMapWithDelay() {
		val finiteFlux = Flux.interval(Duration.ofMillis(100)) // start from 0 -> .... N
				.delayElements(Duration.ofSeconds(1))
				.map { it.toInt() }
				.take(3)
				.log()

		StepVerifier.create(finiteFlux)
				.expectSubscription()
				.expectNext(0, 1, 2)
				.verifyComplete()

	}
}