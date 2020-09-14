package com.hyecheon.learnractivespring.fluxandmonoplayground

import org.junit.jupiter.api.*
import reactor.core.publisher.*
import reactor.test.*

class FluxAndMonoFactoryTest {

	val names = listOf("adam", "anna", "jack", "jenny")

	@Test
	internal fun fluxUsingIterable() {
		val namesFlux = Flux.fromIterable(names)
				.log()

		StepVerifier.create(namesFlux)
				.expectNext("adam", "anna", "jack", "jenny")
				.verifyComplete()
	}

	@Test
	internal fun fluxUsingArray() {

		val names = this.names.toTypedArray()

		val namesFlux = Flux.fromArray(names)

		StepVerifier.create(namesFlux)
				.expectNext("adam", "anna", "jack", "jenny")
				.verifyComplete()

	}

	@Test
	internal fun fluxUsingStream() {
		val namesFlux = Flux.fromStream(names.stream())

		StepVerifier.create(namesFlux)
				.expectNext("adam", "anna", "jack", "jenny")
				.verifyComplete()
	}

	@Test
	internal fun monoUsingJustOrEmpty() {
		val mono = Mono.justOrEmpty<String>(null)
		StepVerifier.create(mono)
				.verifyComplete()
	}

	@Test
	internal fun monoUsingSupplier() {
		val stringSupplier = { "adam" }

		val stringMono = Mono.fromSupplier(stringSupplier)
		println(stringSupplier())
		StepVerifier.create(stringMono.log())
				.expectNext("adam")
				.verifyComplete()
	}

	@Test
	internal fun fluxUsingRange() {
		val integerFlux = Flux.range(1, 5).log()

		StepVerifier.create(integerFlux)
				.expectNext(1, 2, 3, 4, 5)
				.verifyComplete()
	}

}