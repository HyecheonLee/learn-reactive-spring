package com.hyecheon.learnractivespring.router

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
}