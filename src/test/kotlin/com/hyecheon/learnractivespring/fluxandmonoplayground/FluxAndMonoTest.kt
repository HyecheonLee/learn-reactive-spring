package com.hyecheon.learnractivespring.fluxandmonoplayground

import org.junit.jupiter.api.*
import reactor.core.publisher.*
import reactor.test.*

class FluxAndMonoTest {
	@Test
	internal fun fluxTest() {

		val stringFlux = Flux
				.just("Spring", "Spring boot", "Reactive Spring")
//				.concatWith(Flux.error(RuntimeException("Exception Occurred")))
				.concatWith(Flux.just("After Error"))
				.log()

		stringFlux
				.subscribe(
						::println,
						{ e ->
							System.err.println("Exception is $e")
						},
						{ println("Completed") })
	}

	@Test
	internal fun fluxTestElementsWithoutError() {
		val stringFlux = Flux
				.just("Spring", "Spring boot", "Reactive Spring")
				.log()

		StepVerifier
				.create(stringFlux)
				.expectNext("Spring")
				.expectNext("Spring boot")
				.expectNext("Reactive Spring")
				.verifyComplete()

	}

	@Test
	internal fun fluxTestElementsWithError() {
		val stringFlux = Flux
				.just("Spring", "Spring boot", "Reactive Spring")
				.concatWith(Flux.error(RuntimeException("Exception Occurred")))
				.log()

		StepVerifier
				.create(stringFlux)
				.expectNext("Spring")
				.expectNext("Spring boot")
				.expectNext("Reactive Spring")
//				.expectError(RuntimeException::class)
				.expectErrorMessage("Exception Occurred")
				.verify()
	}

	@Test
	internal fun fluxTestElementsWithError1() {
		val stringFlux = Flux
				.just("Spring", "Spring boot", "Reactive Spring")
				.concatWith(Flux.error(RuntimeException("Exception Occurred")))
				.log()

		StepVerifier
				.create(stringFlux)
				.expectNext("Spring", "Spring boot", "Reactive Spring")
				.expectErrorMessage("Exception Occurred")
				.verify()
	}

	@Test
	internal fun fluxTestElementsCountWithError() {
		val stringFlux = Flux
				.just("Spring", "Spring boot", "Reactive Spring")
				.concatWith(Flux.error(RuntimeException("Exception Occurred")))
				.log()

		StepVerifier
				.create(stringFlux)
				.expectNextCount(3)
				.expectErrorMessage("Exception Occurred")
				.verify()
	}

	@Test
	internal fun monoTest() {

		val stringMono = Mono.just("Spring")

		StepVerifier
				.create(stringMono.log())
				.expectNext("Spring")
				.verifyComplete()
	}

	@Test
	internal fun monoTestError() {

		StepVerifier
				.create(Mono.error<RuntimeException>(RuntimeException("Exception Occurred")).log())
				.expectError(RuntimeException::class.java)
				.verify()

	}
}