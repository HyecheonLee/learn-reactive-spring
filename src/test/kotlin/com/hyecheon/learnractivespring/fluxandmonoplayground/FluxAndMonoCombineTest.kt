package com.hyecheon.learnractivespring.fluxandmonoplayground

import org.junit.jupiter.api.*
import reactor.core.publisher.*
import reactor.test.*
import reactor.test.scheduler.*
import java.time.*
import java.util.function.*


class FluxAndMonoCombineTest {
	@Test
	internal fun combineUsingMerge() {

		val flux1 = Flux.just("A", "B", "C")
		val flux2 = Flux.just("D", "E", "F")

		val mergeFlux = Flux.merge(flux1, flux2)

		StepVerifier.create(mergeFlux.log())
				.expectSubscription()
				.expectNext("A", "B", "C", "D", "E", "F")
				.verifyComplete()
	}

	@Test
	internal fun combineUsingMergeWithDelay() {
		val flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1))
		val flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1))

		val mergeFlux = Flux.merge(flux1, flux2)

		StepVerifier.create(mergeFlux.log())
				.expectSubscription()
				.expectNextCount(6)
//				.expectNext("A", "B", "C", "D", "E", "F")
				.verifyComplete()
	}

	@Test
	internal fun combineUsingConcat() {

		val flux1 = Flux.just("A", "B", "C")
		val flux2 = Flux.just("D", "E", "F")

		val mergeFlux = Flux.concat(flux1, flux2)

		StepVerifier.create(mergeFlux.log())
				.expectSubscription()
				.expectNext("A", "B", "C", "D", "E", "F")
				.verifyComplete()
	}

	@Test
	internal fun combineUsingConcatWithDelay() {
		VirtualTimeScheduler.getOrSet()

		val flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1))
		val flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1))

		val mergeFlux = Flux.concat(flux1, flux2)

		StepVerifier.withVirtualTime { mergeFlux.log() }
				.expectSubscription()
				.thenAwait(Duration.ofSeconds(6))
				.expectNextCount(6)
				.verifyComplete()

		/*StepVerifier.create(mergeFlux.log())
				.expectSubscription()
				.expectNext("A", "B", "C", "D", "E", "F")
				.verifyComplete()*/
	}

	@Test
	internal fun combineUsingZip() {

		val flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1))
		val flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1))
		val mergedFlux: Flux<String> = Flux.zip(flux1, flux2, BiFunction { t, u -> t + u })


		StepVerifier.create(mergedFlux.log())
				.expectSubscription()
				.expectNext("AD", "BE", "CF")
				.verifyComplete()
	}

}