package com.hyecheon.learnractivespring.fluxandmonoplayground

class CustomException(e: Throwable) : Throwable() {
	override var message = e.message
}
