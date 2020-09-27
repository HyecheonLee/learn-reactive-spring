package com.hyecheon.learnractivespring.exception

import com.hyecheon.learnractivespring.controller.v1.*
import org.slf4j.*
import org.springframework.http.*
import org.springframework.web.bind.annotation.*

@ControllerAdvice
class ControllerExceptionHandler {
	private val log: Logger = LoggerFactory.getLogger(this::class.java)

	@ExceptionHandler(RuntimeException::class)
	fun handleRuntimeException(ex: RuntimeException) = run {
		log.error("Exception caught in handleRuntimeException : $ex")
		ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.message)
	}

	@ExceptionHandler(Exception::class)
	fun handleException(ex: Exception) {
		log.error("Exception caught in handleException : $ex")
		ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.message)
	}
}