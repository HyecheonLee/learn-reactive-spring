package com.hyecheon.learnractivespring.handler

import com.hyecheon.learnractivespring.document.*
import com.hyecheon.learnractivespring.repository.*
import org.springframework.http.*
import org.springframework.stereotype.*
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.EntityResponse.*


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

	companion object {
		val notFound = ServerResponse.notFound().build()
	}
}