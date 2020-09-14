package com.hyecheon.learnractivespring.controller

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.*
import org.springframework.beans.factory.annotation.*
import org.springframework.boot.test.autoconfigure.web.reactive.*
import org.springframework.boot.test.context.*
import org.springframework.http.*
import org.springframework.test.annotation.*
import org.springframework.test.context.junit.jupiter.*
import org.springframework.test.web.reactive.server.*
import org.springframework.test.web.reactive.server.WebTestClient.*
import reactor.test.*
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureWebTestClient
@DirtiesContext
internal class FluxAndMonoControllerTest {

	@Autowired
	lateinit var webTestClient: WebTestClient

	@Test
	internal fun fluxApproach() {
		val integerFlux = webTestClient.get().uri("/flux")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus()
				.isOk
				.returnResult(Int::class.java)
				.responseBody

		StepVerifier.create(integerFlux)
				.expectSubscription()
				.expectNext(1)
				.expectNext(2)
				.expectNext(3)
				.expectNext(4)
				.verifyComplete()

	}


	@Test
	internal fun fluxApproach2() {

		webTestClient.get().uri("/flux")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus()
				.isOk
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBodyList(Int::class.java)
				.hasSize(4)
	}

	@Test
	internal fun fluxApproach3() {

		val expectedIntegerList = listOf(1, 2, 3, 4)

		val entityExchangeResult = webTestClient.get().uri("/flux")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus()
				.isOk
				.expectBodyList(Int::class.java)
				.returnResult()

		assertEquals(expectedIntegerList, entityExchangeResult.responseBody)
	}

	@Test
	internal fun fluxApproach4() {

		val expectedIntegerList = Arrays.asList(1, 2, 3, 4)
		webTestClient.get().uri("/flux")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus()
				.isOk
				.expectBodyList(Int::class.java)
				.consumeWith<ListBodySpec<Int>> { response ->
					assertEquals(expectedIntegerList, response.responseBody)
				}
	}

	@Test
	internal fun fluxStream() {

		val longStreamFlux = webTestClient.get().uri("/fluxstream")
				.accept(MediaType.APPLICATION_STREAM_JSON)
				.exchange()
				.expectStatus().isOk
				.returnResult(Long::class.java)
				.responseBody

		StepVerifier.create(longStreamFlux)
				.expectNext(0L)
				.expectNext(1L)
				.expectNext(2L)
				.thenCancel()
				.verify()
	}

	@Test
	internal fun mono() {
		val expectedValue: Int = 1
		val expectBody = webTestClient.get().uri("/mono")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk
				.expectBody(Int::class.java)
		val returnResult = expectBody.returnResult()
		assertEquals(expectedValue, returnResult.responseBody)
	}
}