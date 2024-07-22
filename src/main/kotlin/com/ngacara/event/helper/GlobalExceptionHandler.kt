package com.ngacara.event.helper

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.ngacara.event.models.ApiResponse
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.server.ResponseStatusException

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<ApiResponse<Nothing>> {
        val errors = ex.bindingResult.fieldErrors
            .joinToString(separator = ", ") { "${it.field}: ${it.defaultMessage ?: "Invalid field"}" }

        val response = createApiResponse(
            statusCode = HttpStatus.BAD_REQUEST.value(),
            message = errors.ifEmpty { "Validation error" },
            data = null
        )
        return ResponseEntity.badRequest().body(response)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(ex: HttpMessageNotReadableException): ResponseEntity<ApiResponse<Nothing>> {
        val errors = (ex.cause as? MismatchedInputException)?.path
            ?.mapNotNull { it.fieldName?.let { fieldName -> "Field '$fieldName' is missing or null" } }
            ?: emptyList()

        val message =
            errors.takeIf { it.isNotEmpty() }?.joinToString(separator = ", ") ?: ex.message ?: "Invalid request format"

        val response = createApiResponse(
            statusCode = HttpStatus.BAD_REQUEST.value(),
            message = message,
            data = null
        )
        return ResponseEntity.badRequest().body(response)
    }


    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolationException(ex: DataIntegrityViolationException): ResponseEntity<ApiResponse<Nothing>> {
        val message = ex.message ?: "Database error"
        val response = createApiResponse(
            statusCode = HttpStatus.CONFLICT.value(),
            message = extractSpecificErrorMessage(message),
            data = null
        )
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response)
    }

    @ExceptionHandler(UsernameNotFoundException::class)
    fun handleUsernameNotFoundException(ex: UsernameNotFoundException): ResponseEntity<ApiResponse<Nothing>> {
        val response = createApiResponse(
            statusCode = HttpStatus.NOT_FOUND.value(),
            message = ex.message.toString(),
            data = null
        )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
    }

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFoundException(ex: ResourceNotFoundException): ResponseEntity<ApiResponse<Nothing>> {
        val response = createApiResponse(
            statusCode = ex.status.value(),
            message = ex.message.toString(),
            data = null
        )
        return ResponseEntity.status(ex.status).body(response)
    }
    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ApiResponse<Nothing>> {
        val response = createApiResponse(
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            message = ex.message.toString(),
            data = null
        )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response)
    }

    private fun extractSpecificErrorMessage(errorMessage: String): String {
        return when {
            errorMessage.contains("email") -> "Email already exists"
            errorMessage.contains("username") -> "Username already exists"
            else -> "Duplicate key error"
        }
    }
}