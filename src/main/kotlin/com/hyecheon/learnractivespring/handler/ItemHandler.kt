package com.hyecheon.learnractivespring.handler

import com.hyecheon.learnractivespring.document.*
import com.hyecheon.learnractivespring.repository.*
import org.springframework.http.*
import org.springframework.stereotype.*
import org.springframework.web.reactive.function.server.*


@Component
class ItemHandler(
		val itemReactiveRepository: ItemReactiveRepository) {

	fun getAllItems(serverRequest: ServerRequest) = run {

		ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(itemReactiveRepository.findAll(), Item::class.java)

	}
}