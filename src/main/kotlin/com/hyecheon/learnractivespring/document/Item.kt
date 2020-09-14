package com.hyecheon.learnractivespring.document

import org.springframework.data.annotation.*
import org.springframework.data.mongodb.core.mapping.*

@Document // @Entity
data class Item(
		@Id
		var id: String? = null,
		var description: String? = null,
		var price: Double? = null
)