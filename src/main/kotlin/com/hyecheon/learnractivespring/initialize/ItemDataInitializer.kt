package com.hyecheon.learnractivespring.initialize

import com.hyecheon.learnractivespring.document.*
import com.hyecheon.learnractivespring.repository.*
import org.springframework.beans.factory.annotation.*
import org.springframework.boot.*
import org.springframework.context.annotation.*
import org.springframework.stereotype.*
import reactor.core.publisher.*

@Component
@Profile("!test")
class ItemDataInitializer : CommandLineRunner {

	@Autowired
	lateinit var itemReactiveRepository: ItemReactiveRepository

	override fun run(vararg args: String?) {
		initialDataSetUp()
	}

	fun data() = run {
		listOf(
				Item(null, "Samsung TV", 399.99),
				Item(null, "LG TV", 329.99),
				Item(null, "AppleWatch TV", 349.99),
				Item("ABC", "Beats HeadPhones TV", 19.99)
		)
	}

	fun initialDataSetUp() {
		itemReactiveRepository.deleteAll()
				.thenMany(Flux.fromIterable(data()))
				.flatMap { itemReactiveRepository.save(it) }
				.thenMany(itemReactiveRepository.findAll())
				.subscribe { it ->
					println("Item inserted from CommandLineRunner : $it")
				}

	}
}