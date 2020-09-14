package com.hyecheon.learnractivespring.fluxandmonoplayground

import org.junit.jupiter.api.*
import reactor.core.publisher.*
import reactor.test.*
import reactor.util.retry.*
import java.time.*

class FluxAndMonoErrorTest {

	@Test
	internal fun fluxErrorHandling() {
		val stringFlux = Flux.just("A", "B", "C")
				.concatWith(Flux.error(RuntimeException("Exception Occurred")))
				.concatWith(Flux.just("D"))
				.onErrorResume { e -> // this block gets executed
					println("Exception is : $e")
					Flux.just("default", "default1")
				}

		StepVerifier.create(stringFlux)
				.expectSubscription()
				.expectNext("A", "B", "C")
//				.expectError(RuntimeException::class.java)
//				.verify()
				.expectNext("default", "default1")
				.verifyComplete()
	}

	@Test
	internal fun fluxErrorHandlingOnErrorReturn() {
		val stringFlux = Flux.just("A", "B", "C")
				.concatWith(Flux.error(RuntimeException("Exception Occurred")))
				.concatWith(Flux.just("D"))
				.onErrorReturn("default")

		StepVerifier.create(stringFlux)
				.expectSubscription()
				.expectNext("A", "B", "C")
//				.expectError(RuntimeException::class.java)
//				.verify()
				.expectNext("default")
				.verifyComplete()
	}

	@Test
	internal fun fluxErrorHandlingOnErrorMap() {
		val stringFlux = Flux.just("A", "B", "C")
				.concatWith(Flux.error(RuntimeException("Exception Occurred")))
				.concatWith(Flux.just("D"))
				.onErrorMap { e -> CustomException(e) }

		StepVerifier.create(stringFlux)
				.expectSubscription()
				.expectNext("A", "B", "C")
				.expectError(CustomException::class.java)
				.verify()
	}

	@Test
	internal fun fluxErrorHandlingOnErrorMapWithRetry() {
		val stringFlux = Flux.just("A", "B", "C")
				.concatWith(Flux.error(RuntimeException("Exception Occurred")))
				.concatWith(Flux.just("D"))
				.onErrorMap { e -> CustomException(e) }
				.retry(2)

		StepVerifier.create(stringFlux.log())
				.expectSubscription()
				.expectNext("A", "B", "C")
				.expectNext("A", "B", "C")
				.expectNext("A", "B", "C")
				.expectError(CustomException::class.java)
				.verify()
	}

	@Test
	internal fun fluxErrorHandlingOnErrorMapWithRetryBackoff() {
		val stringFlux = Flux.just("A", "B", "C")
				.concatWith(Flux.error(RuntimeException("Exception Occurred")))
				.concatWith(Flux.just("D"))
				.onErrorMap { e -> CustomException(e) }
				.retryWhen(Retry.backoff(2, Duration.ofSeconds(2)))

		StepVerifier.create(stringFlux.log())
				.expectSubscription()
				.expectNext("A", "B", "C")
				.expectNext("A", "B", "C")
				.expectNext("A", "B", "C")
				.expectError(CustomException::class.java)
				.verify()
	}

}