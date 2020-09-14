package com.hyecheon.learnractivespring.fluxandmonoplayground

import org.junit.jupiter.api.*
import reactor.core.publisher.*
import reactor.test.*

class FluxAndMonoBackPressureTest {
	@Test
	internal fun backPressureTest() {
		val finiteFlux = Flux.range(1, 10)
				.log()

		StepVerifier.create(finiteFlux)
				.expectSubscription()
				.thenRequest(1)
				.expectNext(1)
				.thenRequest(1)
				.expectNext(2)
				.thenCancel()
				.verify()
	}

	@Test
	internal fun backPressure() {
		val finiteFlux = Flux.range(1, 10)
				.log()
		finiteFlux.subscribe(
				{ e -> println("Element is : $e") },
				{ e -> println("Exception is : $e") },
				{ println("Done") },
				{ s -> s.request(2) }
		)
	}

	@Test
	internal fun backPressureCancel() {
		val finiteFlux = Flux.range(1, 10)
				.log()
		finiteFlux.subscribe(
				{ e -> println("Element is : $e") },
				{ e -> println("Exception is : $e") },
				{ println("Done") },
				{ s -> s.cancel() }
		)
	}

	@Test
	internal fun customizedBackPressure() {
		val finiteFlux = Flux.range(1, 10)
				.log()

		finiteFlux.subscribe(object : BaseSubscriber<Int>() {
			override fun hookOnNext(value: Int) {
				request(1)
				println("Value received is : $value")
				if (value == 4) {
					cancel()
				}
			}
		})
	}


}