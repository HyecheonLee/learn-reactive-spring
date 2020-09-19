package com.hyecheon.learnractivespring.handler

import com.hyecheon.learnractivespring.document.*
import com.hyecheon.learnractivespring.repository.*
import org.springframework.http.*
import org.springframework.stereotype.*
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.EntityResponse.*
import reactor.core.publisher.*


@Component
class ItemHandler(
		val itemReactiveRepository: ItemReactiveRepository) {

	fun getAllItems(serverRequest: ServerRequest) = run {

		ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(itemReactiveRepository.findAll(), Item::class.java)

	}

	fun getOneItem(serverRequest: ServerRequest) = run {
		val id = serverRequest.pathVariable("id")
		itemReactiveRepository.findById(id).flatMap { item ->
			ServerResponse.ok()
					.contentType(MediaType.APPLICATION_JSON)
					.body<Any>(fromObject(item))
					.switchIfEmpty(notFound)
		}
	}

	fun createItem(serverRequest: ServerRequest): Mono<out ServerResponse> {
		val itemTobeInserted = serverRequest.bodyToMono(Item::class.java)
		return itemTobeInserted.flatMap { item ->
			ServerResponse.ok()
					.contentType(MediaType.APPLICATION_JSON)
					.body<Item>(itemReactiveRepository.save(item))
		}
	}

	fun deleteItem(serverRequest: ServerRequest): Mono<out ServerResponse> {
		val id = serverRequest.pathVariable("id")
		val deleteItem = itemReactiveRepository.deleteById(id)
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(deleteItem)
	}

	fun updateItem(serverRequest: ServerRequest): Mono<out ServerResponse> {
		val id = serverRequest.pathVariable("id")
		val updatedItem = serverRequest.bodyToMono(Item::class.java)
				.flatMap { item ->
					itemReactiveRepository.findById(id)
							.flatMap { currentItem ->
								itemReactiveRepository.save(currentItem.copy(description = item.description, price = item.price))
							}
				}
		return updatedItem.flatMap {
			ServerResponse
					.ok()
					.contentType(MediaType.APPLICATION_JSON)
					.body<Any>(fromObject(it))
					.switchIfEmpty(notFound)
		}
	}

	companion object {
		val notFound = ServerResponse.notFound().build()
	}
}