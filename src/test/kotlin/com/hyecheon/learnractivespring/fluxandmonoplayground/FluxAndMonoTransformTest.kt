package com.hyecheon.learnractivespring.fluxandmonoplayground

import org.junit.jupiter.api.*
import reactor.core.publisher.*
import reactor.core.scheduler.Schedulers.*
import reactor.test.*
import java.lang.Thread.*

class FluxAndMonoTransformTest {
	val names = listOf("adam", "anna", "jack", "jenny")

	@Test
	internal fun transformUsingMap() {
		val namesFlux = Flux.fromIterable(names)
				.map { it.toUpperCase() } // adam, anna, jack, jenny
				.log()

		StepVerifier.create(namesFlux)
				.expectNext("ADAM", "ANNA", "JACK", "JENNY")
				.verifyComplete()

	}

	@Test
	internal fun transformUsingMapLength() {
		val namesFlux = Flux.fromIterable(names)
				.map { it.length } // adam, anna, jack, jenny
				.log()

		StepVerifier.create(namesFlux)
				.expectNext(4, 4, 4, 5)
				.verifyComplete()

	}

	@Test
	internal fun transformUsingMapLengthRepeat() {
		val namesFlux = Flux.fromIterable(names)
				.map { it.length } // adam, anna, jack, jenny
				.repeat(1)
				.log()

		StepVerifier.create(namesFlux)
				.expectNext(4, 4, 4, 5)
				.expectNext(4, 4, 4, 5)
				.verifyComplete()
	}

	@Test
	internal fun transformUsingMapFilter() {
		val namesFlux = Flux.fromIterable(names)
				.filter { it.length > 4 }
				.map { it.toUpperCase() } // adam, anna, jack, jenny
				.log()

		StepVerifier.create(namesFlux)
				.expectNext("JENNY")
				.verifyComplete()
	}

	@Test
	internal fun transformUsingFlatMap() {
		val stringFlux = Flux.fromIterable(listOf("A", "B", "C", "D", "E", "F"))// A,B,C,D,E,F
				.flatMap { s ->
					Flux.fromIterable(convertToList(s)) // A -> List[A,newValue],B-> List[B,newValue]
				} // db or external service call that returns a flux -> s -> Flux<String>
				.log()

		StepVerifier
				.create(stringFlux)
				.expectNextCount(12)
				.verifyComplete()
	}

	private fun convertToList(s: String): List<String> {
		sleep(1000)
		return listOf(s, "newValue")
	}

	@Test
	internal fun transformUsingFlatMapUsingParallel() {
		val stringFlux = Flux.fromIterable(listOf("A", "B", "C", "D", "E", "F"))// A,B,C,D,E,F
				.window(2) // Flux<Flux<String>> (A,B),(C,D),(E,F)
				.flatMap { s ->
					s.map { this.convertToList(it) }.subscribeOn(parallel())// Flux<List<String>>
				} // db or external service call that returns a flux -> s -> Flux<String>
				.flatMap { s -> Flux.fromIterable(s) }
				.log()

		StepVerifier
				.create(stringFlux)
				.expectNextCount(12)
				.verifyComplete()
	}

	@Test
	internal fun transformUsingFlatMapUsingParallelMaintainOrder() {
		val stringFlux = Flux.fromIterable(listOf("A", "B", "C", "D", "E", "F"))// A,B,C,D,E,F
				.window(2) // Flux<Flux<String>> (A,B),(C,D),(E,F)
				/*.concatMap { s ->
					s.map { this.convertToList(it) }.subscribeOn(parallel())// Flux<List<String>>
				} // db or external service call that returns a flux -> s -> Flux<String>*/
				.flatMap { s ->
					s.map { this.convertToList(it) }.subscribeOn(parallel())// Flux<List<String>>
				} // db or external service call that returns a flux -> s -> Flux<String>
				.flatMap { s -> Flux.fromIterable(s) }
				.log()
		StepVerifier
				.create(stringFlux)
				.expectNextCount(12)
				.verifyComplete()
	}
}