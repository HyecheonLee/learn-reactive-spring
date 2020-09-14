package com.hyecheon.learnractivespring.controller.v1

import com.hyecheon.learnractivespring.constants.*
import com.hyecheon.learnractivespring.document.*
import com.hyecheon.learnractivespring.repository.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
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

@SpringBootTest
@ExtendWith(SpringExtension::class)
@DirtiesContext
@AutoConfigureWebTestClient
@ActiveProfiles("test")
internal class ItemControllerTest {

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
				.uri(ItemConstants.ITEM_END_POINT_V1)
				.exchange()
				.expectStatus().isOk
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBodyList(Item::class.java)
				.hasSize(4)
	}

	@Test
	internal fun getAllItemsApproach2() {
		val returnResult = webTestClient.get()
				.uri(ItemConstants.ITEM_END_POINT_V1)
				.exchange()
				.expectStatus().isOk
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBodyList(Item::class.java)
				.hasSize(4)
				.returnResult()
		returnResult.responseBody?.forEach { item ->
			assertTrue(item.id != null)
		}
	}

	@Test
	internal fun getAllItemsApproach3() {
		val itemsFlux = webTestClient.get()
				.uri(ItemConstants.ITEM_END_POINT_V1)
				.exchange()
				.expectStatus().isOk
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.returnResult(Item::class.java)
				.responseBody

		StepVerifier.create(itemsFlux.log("value from network : "))
				.expectNextCount(4)
				.verifyComplete()

	}

	@Test
	internal fun getOneItem() {
		webTestClient.get().uri("${ItemConstants.ITEM_END_POINT_V1}/{id}", "ABC")
				.exchange()
				.expectStatus().isOk
				.expectBody()
				.jsonPath("$.price", "149.99")
	}

	@Test
	internal fun getOneItemNotFound() {
		webTestClient.get().uri("${ItemConstants.ITEM_END_POINT_V1}/{id}", "DEF")
				.exchange()
				.expectStatus().isNotFound

	}

	@Test
	internal fun createItem() {

		val item = Item(null, "Iphone X", 999.99)

		webTestClient.post().uri(ItemConstants.ITEM_END_POINT_V1)
				.contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(item), Item::class.java)
				.exchange()
				.expectStatus().isCreated
				.expectBody()
				.jsonPath("$.id").isNotEmpty
				.jsonPath("$.description").isEqualTo("Iphone X")
				.jsonPath("$.price").isEqualTo(999.99)

	}

	@Test
	internal fun deleteItem() {

		webTestClient.delete().uri(ItemConstants.ITEM_END_POINT_V1 + "/{id}", "ABC")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk
				.expectBody(Any::class.java)
	}

	@Test
	internal fun updateItem() {
		val newPrice = 129.99
		val item = Item(null, "Beats HeadPhones TV", newPrice)
		webTestClient.put().uri("${ItemConstants.ITEM_END_POINT_V1}/{id}", "ABC")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.body(Mono.just(item), Item::class.java)
				.exchange()
				.expectStatus().isOk
				.expectBody()
				.jsonPath("$.price").isEqualTo(129.99)

	}

	@Test
	internal fun updateItemNotFound() {
		val item = Item(null, "Beats HeadPhones TV", 149.99)
		webTestClient.put().uri("${ItemConstants.ITEM_END_POINT_V1}/{id}", "DEF")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.body(Mono.just(item), Item::class.java)
				.exchange()
				.expectStatus().isNotFound
	}
}