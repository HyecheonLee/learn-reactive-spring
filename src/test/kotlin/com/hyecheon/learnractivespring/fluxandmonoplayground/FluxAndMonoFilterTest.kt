package com.hyecheon.learnractivespring.fluxandmonoplayground

import org.junit.jupiter.api.*
import reactor.core.publisher.*
import reactor.test.*

class FluxAndMonoFilterTest {
	val names = listOf("adam", "anna", "jack", "jenny")

	@Test
	internal fun filterTest() {
		val namesFlux = Flux.fromIterable(names) // adam, anna, jack, jenny
				.filter { it.startsWith("a") }
				.log()

		StepVerifier.create(namesFlux)
				.expectNext("adam", "anna")
				.verifyComplete()

	}

	@Test
	internal fun filterTestLength() {
		val namesFlux = Flux.fromIterable(names) // adam, anna, jack, jenny
				.filter { it.length > 4 }
				.log()

		StepVerifier.create(namesFlux)
				.expectNext("jenny")
				.verifyComplete()


	}
}