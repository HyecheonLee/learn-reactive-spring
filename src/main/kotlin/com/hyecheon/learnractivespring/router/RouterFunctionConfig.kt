package com.hyecheon.learnractivespring.router

import com.hyecheon.learnractivespring.constants.*
import com.hyecheon.learnractivespring.handler.*
import org.springframework.context.annotation.*
import org.springframework.http.*
import org.springframework.web.reactive.function.server.*

@Configuration
class RouterFunctionConfig {
	@Bean
	fun router(handlerFunction: SampleHandlerFunction) = router {
		accept(MediaType.APPLICATION_JSON).nest {
			GET("/functional/flux") {
				handlerFunction.flux(it)
			}
			GET("/functional/mono") {
				handlerFunction.mono(it)
			}
		}
	}

	@Bean
	fun itemRoute(itemHandler: ItemHandler) = router {
		accept(MediaType.APPLICATION_JSON).nest {
			GET(ItemConstants.ITEM_FUNCTIONAL_END_POINT_V1) { it ->
				itemHandler.getAllItems(it)
			}
			GET("${ItemConstants.ITEM_FUNCTIONAL_END_POINT_V1}/{id}") {
				itemHandler.getOneItem(it)
			}
		}
	}

}