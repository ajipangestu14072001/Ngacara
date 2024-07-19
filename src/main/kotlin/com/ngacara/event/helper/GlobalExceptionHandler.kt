package com.ngacara.event.helper

import com.ngacara.event.models.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<ApiResponse<Nothing>> {
        val errors = ex.bindingResult.allErrors.joinToString { it.defaultMessage ?: "Invalid field" }
        val response = ApiResponse(
            statusCode = 400,
            message = "Validation error: $errors",
            data = null
        )
        return ResponseEntity.badRequest().body(response)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(ex: HttpMessageNotReadableException): ResponseEntity<ApiResponse<Nothing>> {
        val response = ApiResponse(
            statusCode = 400,
            message = "Invalid request format: ${ex.message}",
            data = null
        )
        return ResponseEntity.badRequest().body(response)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ApiResponse<Nothing>> {
        val response = ApiResponse(
            statusCode = 500,
            message = "Internal server error: ${ex.message}",
            data = null
        )
        return ResponseEntity.status(500).body(response)
    }
}
