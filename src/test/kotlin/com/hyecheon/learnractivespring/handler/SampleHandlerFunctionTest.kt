package com.hyecheon.learnractivespring.handler

import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.*
import org.springframework.beans.factory.annotation.*
import org.springframework.boot.test.autoconfigure.web.reactive.*
import org.springframework.boot.test.context.*
import org.springframework.http.*
import org.springframework.test.annotation.*
import org.springframework.test.context.junit.jupiter.*
import org.springframework.test.web.reactive.server.*
import reactor.test.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureWebTestClient
@DirtiesContext
internal class SampleHandlerFunctionTest {
	@Autowired
	lateinit var webTestClient: WebTestClient

	@Test
	internal fun fluxApproach() {
		val integerFlux = webTestClient.get().uri("/functional/flux")
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

		webTestClient.get().uri("/functional/flux")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus()
				.isOk
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBodyList(Int::class.java)
				.hasSize(4)
	}

	@Test
	internal fun mono() {
		val expectedValue: Int = 1
		val expectBody = webTestClient.get().uri("/functional/mono")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk
				.expectBody(Int::class.java)
		val returnResult = expectBody.returnResult()
		Assertions.assertEquals(expectedValue, returnResult.responseBody)
	}
}