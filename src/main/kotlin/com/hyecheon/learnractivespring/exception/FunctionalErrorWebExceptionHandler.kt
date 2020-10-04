package com.hyecheon.learnractivespring.exception

import org.slf4j.*
import org.springframework.boot.autoconfigure.web.*
import org.springframework.boot.autoconfigure.web.reactive.error.*
import org.springframework.boot.web.error.*
import org.springframework.boot.web.reactive.error.*
import org.springframework.context.*
import org.springframework.http.*
import org.springframework.http.codec.*
import org.springframework.stereotype.*
import org.springframework.web.reactive.function.*
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.RequestPredicates.*
import reactor.core.publisher.*

@Component
class FunctionalErrorWebExceptionHandler(errorAttributes: ErrorAttributes,
                                         applicationContext: ApplicationContext,
                                         serverCodecConfigurer: ServerCodecConfigurer) :
		AbstractErrorWebExceptionHandler(
				errorAttributes,
				ResourceProperties(),
				applicationContext) {
	init {
		super.setMessageWriters(serverCodecConfigurer.writers)
		super.setMessageReaders(serverCodecConfigurer.readers)
	}

	private val log: Logger = LoggerFactory.getLogger(this::class.java)

	override fun getRoutingFunction(errorAttributes: ErrorAttributes?): RouterFunction<ServerResponse> {
		return RouterFunctions.route(all(), HandlerFunction<ServerResponse> { request ->
			renderErrorResponse(request)
		})
	}

	private fun renderErrorResponse(serverRequest: ServerRequest): Mono<ServerResponse> {
		val errorAttributesMap = getErrorAttributes(serverRequest, ErrorAttributeOptions.defaults())
		log.info("errorAttributesMap : $errorAttributesMap")
		return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromObject(errorAttributesMap["message"]!!))
	}
}