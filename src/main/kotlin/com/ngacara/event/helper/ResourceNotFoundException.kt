package com.ngacara.event.helper

import org.springframework.http.HttpStatus

class ResourceNotFoundException(message: String) : RuntimeException(message) {
    val status: HttpStatus = HttpStatus.NOT_FOUND
}
