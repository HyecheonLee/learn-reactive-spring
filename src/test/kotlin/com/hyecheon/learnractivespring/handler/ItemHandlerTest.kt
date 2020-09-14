package com.hyecheon.learnractivespring.handler

import com.hyecheon.learnractivespring.constants.*
import com.hyecheon.learnractivespring.document.*
import com.hyecheon.learnractivespring.repository.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.*
import org.springframework.beans.factory.annotation.*
import org.springframework.boot.test.autoconfigure.web.reactive.*
import org.springframework.boot.test.context.*
import org.springframework.http.*
import org.springframework.test.annotation.*
import org.springframework.test.context.*
import org.springframework.test.context.junit.jupiter.*
import org.springframework.test.web.reactive.server.*
import reactor.core.publisher.*
import reactor.test.*


@ExtendWith(SpringExtension::class)
@SpringBootTest
@DirtiesContext
@AutoConfigureWebTestClient
@ActiveProfiles("test")
internal class ItemHandlerTest {
	@Autowired
	lateinit var webTestClient: WebTestClient

	@Autowired
	lateinit var itemReactiveRepository: ItemReactiveRepository

	fun data() = run {
		listOf(
				Item(null, "Samsung TV", 399.99),
				Item(null, "LG TV", 329.99),
				Item(null, "AppleWatch TV", 349.99),
				Item("ABC", "Beats HeadPhones TV", 149.99)
		)
	}

	@BeforeEach
	internal fun setUp() {
		itemReactiveRepository.deleteAll()
				.thenMany(Flux.fromIterable(data()))
				.flatMap { itemReactiveRepository.save(it) }
				.doOnNext { item ->
					println("Inserted item is : $item")
				}
				.blockLast()

	}

	@Test
	internal fun getAllItems() {
		webTestClient.get()
				.uri(ItemConstants.ITEM_FUNCTIONAL_END_POINT_V1)
				.exchange()
				.expectStatus().isOk
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBodyList(Item::class.java)
				.hasSize(4)
	}

	@Test
	internal fun getAllItemsApproach2() {
		val returnResult = webTestClient.get()
				.uri(ItemConstants.ITEM_FUNCTIONAL_END_POINT_V1)
				.exchange()
				.expectStatus().isOk
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBodyList(Item::class.java)
				.hasSize(4)
				.returnResult()
		returnResult.responseBody?.forEach { item ->
			Assertions.assertTrue(item.id != null)
		}
	}

	@Test
	internal fun getAllItemsApproach3() {
		val itemsFlux = webTestClient.get()
				.uri(ItemConstants.ITEM_FUNCTIONAL_END_POINT_V1)
				.exchange()
				.expectStatus().isOk
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.returnResult(Item::class.java)
				.responseBody

		StepVerifier.create(itemsFlux.log("value from network : "))
				.expectNextCount(4)
				.verifyComplete()
	}
}