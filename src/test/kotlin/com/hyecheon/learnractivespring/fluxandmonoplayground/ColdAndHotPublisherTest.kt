package com.hyecheon.learnractivespring.fluxandmonoplayground

import org.junit.jupiter.api.*
import reactor.core.publisher.*
import java.lang.Thread.*
import java.time.*

class ColdAndHotPublisherTest {
	@Test
	internal fun coldPublisherTest() {
		val stringFlux = Flux.just("A", "B", "C", "D", "E", "F")
				.delayElements(Duration.ofSeconds(1))

		stringFlux.subscribe {
			println("Subscriber 1 : $it")
		} // emits the value from beginning

		sleep(2000)

		stringFlux.subscribe {
			println("Subscriber 2 : $it")
		} // emits the value from beginning

		sleep(4000)
	}

	@Test
	internal fun hotPublisherTest() {

		val stringFlux = Flux.just("A", "B", "C", "D", "E", "F")
				.delayElements(Duration.ofSeconds(1))

		val connectableFlux = stringFlux.publish()
		connectableFlux.connect()
		connectableFlux.subscribe { s -> println("Subscriber 1 : $s") }
		sleep(3000)

		connectableFlux.subscribe { s -> println("Subscriber 2 : $s") }
		sleep(4000)
	}
}