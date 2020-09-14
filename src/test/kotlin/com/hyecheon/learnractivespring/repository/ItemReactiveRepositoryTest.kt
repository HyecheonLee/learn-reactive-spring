package com.hyecheon.learnractivespring.repository

import com.hyecheon.learnractivespring.document.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.*
import org.springframework.beans.factory.annotation.*
import org.springframework.boot.test.autoconfigure.data.mongo.*
import org.springframework.test.annotation.*
import org.springframework.test.context.junit.jupiter.*
import reactor.core.publisher.*
import reactor.test.*

@DataMongoTest
@ExtendWith(SpringExtension::class)
@DirtiesContext
class ItemReactiveRepositoryTest {
	@Autowired
	lateinit var itemReactiveRepository: ItemReactiveRepository

	private val itemList = mutableListOf(
			Item(null, "Samsung TV", 400.0),
			Item(null, "LG TV", 420.0),
			Item(null, "Apple Watch", 299.99),
			Item(null, "Beats Headphones", 149.99),
			Item("ABC", "Bose Headphones", 149.99)
	)

	@BeforeEach
	internal fun setUp() {
		itemReactiveRepository.deleteAll()
				.thenMany(Flux.fromIterable(itemList))
				.flatMap { itemReactiveRepository.save(it) }
				.doOnNext { item ->
					println("Inserted Item is : $item")
				}
				.blockLast()
	}

	@Test
	internal fun getAllItems() {
		StepVerifier.create(itemReactiveRepository.findAll()) // 5
				.expectSubscription()
				.expectNextCount(5)
				.verifyComplete()
	}

	@Test
	internal fun getItemByID() {
		StepVerifier.create(itemReactiveRepository.findById("ABC")) // 4
				.expectSubscription()
				.expectNextMatches { item ->
					item.description == "Bose Headphones"
				}
				.verifyComplete()
	}

	@Test
	internal fun findItemByDescription() {
		StepVerifier.create(itemReactiveRepository.findByDescription("Bose Headphones").log("findItemByDescription : "))
				.expectSubscription()
				.expectNextCount(1)
				.verifyComplete()
	}

	@Test
	internal fun saveItem() {

		val item = Item("DEF", "Google Home Mini", 30.00)
		val savedItem = itemReactiveRepository.save(item)
		StepVerifier.create(savedItem.log("saveItem : "))
				.expectSubscription()
				.expectNextMatches { it.id != null && item.description == "Google Home Mini" }
				.verifyComplete()
	}

	@Test
	internal fun updateItem() {
		val newPrice = 520.00
		val updatedItem = itemReactiveRepository.findByDescription("LG TV")
				.map {
					it.also { item -> item.price = newPrice }
				}
				.flatMap { itemReactiveRepository.save(it) } // saving the ite with the new price
		StepVerifier.create(updatedItem)
				.expectSubscription()
				.expectNextMatches { it -> it.price == 520.00 }
				.verifyComplete()
	}

	@Test
	internal fun deleteItemById() {
		val deletedItem = itemReactiveRepository.findById("ABC") // Mono<Item>
				.map { item -> item.id!! }
				.flatMap { it ->
					itemReactiveRepository.deleteById(it)
				}

		StepVerifier.create(deletedItem.log())
				.expectSubscription()
				.verifyComplete()

		StepVerifier.create(itemReactiveRepository.findAll().log("The new Item List : "))
				.expectSubscription()
				.expectNextCount(4)
				.verifyComplete()

	}

	@Test
	internal fun deleteItem() {
		val deletedItem = itemReactiveRepository.findByDescription("Bose Headphones") // Mono<Item>
				.flatMap { it -> itemReactiveRepository.delete(it) }

		StepVerifier.create(deletedItem.log())
				.expectSubscription()
				.verifyComplete()

		StepVerifier.create(itemReactiveRepository.findAll().log("The new Item List : "))
				.expectSubscription()
				.expectNextCount(4)
				.verifyComplete()
	}
}