package com.hyecheon.learnractivespring.controller.v1

import com.hyecheon.learnractivespring.constants.ItemConstants.Companion.ITEM_END_POINT_V1
import com.hyecheon.learnractivespring.document.*
import com.hyecheon.learnractivespring.repository.*
import org.slf4j.*
import org.springframework.http.*
import org.springframework.web.bind.annotation.*

@RestController
class ItemController(
		val itemReactiveRepository: ItemReactiveRepository) {
	private val log: Logger = LoggerFactory.getLogger(ItemController::class.java)

	@GetMapping(value = [ITEM_END_POINT_V1])
	fun getAllItems() = run {
		itemReactiveRepository.findAll()
	}

	@GetMapping("$ITEM_END_POINT_V1/{id}")
	fun getOneItem(@PathVariable id: String) = kotlin.run {
		itemReactiveRepository.findById(id)
				.map { t: Item? ->
					ResponseEntity(t, HttpStatus.OK)
				}
				.defaultIfEmpty(ResponseEntity(HttpStatus.NOT_FOUND))
	}

	@PostMapping(ITEM_END_POINT_V1)
	@ResponseStatus(HttpStatus.CREATED)
	fun createItem(@RequestBody item: Item) = run {
		itemReactiveRepository.save(item)
	}

	@DeleteMapping("$ITEM_END_POINT_V1/{id}")
	fun deleteItem(@PathVariable id: String) = run {
		itemReactiveRepository.deleteById(id)
	}

	@PutMapping("$ITEM_END_POINT_V1/{id}")
	fun updateItem(@PathVariable id: String, @RequestBody item: Item) = run {
		itemReactiveRepository.findById(id).flatMap { currentItem ->
			val updatedItem = currentItem.copy(price = item.price, description = item.description)
			itemReactiveRepository.save(updatedItem)
		}.map { updateItem ->
			ResponseEntity(updateItem, HttpStatus.OK)
		}.defaultIfEmpty(ResponseEntity(HttpStatus.NOT_FOUND))
	}
}