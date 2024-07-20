package com.ngacara.event.helper

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.ngacara.event.models.ApiResponse
import com.ngacara.event.models.ErrorDetails
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<ApiResponse<Map<String, String>>> {
        val errors = ex.bindingResult.fieldErrors.associate {
            it.field to (it.defaultMessage ?: "Invalid field")
        }
        val response = createApiResponse(
            statusCode = HttpStatus.BAD_REQUEST.value(),
            message = "Validation error",
            data = errors
        )
        return ResponseEntity.badRequest().body(response)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(ex: HttpMessageNotReadableException): ResponseEntity<ApiResponse<Map<String, String>>> {
        val cause = ex.cause
        val errors = mutableMapOf<String, String>()

        if (cause is MismatchedInputException) {
            cause.path.forEach { reference ->
                val fieldName = reference.fieldName
                if (fieldName != null) {
                    errors[fieldName] = "Field '$fieldName' is missing or null"
                }
            }
        }

        val response = createApiResponse(
            statusCode = HttpStatus.BAD_REQUEST.value(),
            message = "Invalid request format",
            data = errors.takeIf { it.isNotEmpty() } ?: mapOf("error" to ex.message.toString())
        )
        return ResponseEntity.badRequest().body(response)
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolationException(ex: DataIntegrityViolationException): ResponseEntity<ApiResponse<Map<String, String>>> {
        val errors = mutableMapOf<String, String>()

        val message = ex.message ?: "Database error"
        errors["error"] = extractSpecificErrorMessage(message)

        val response = createApiResponse(
            statusCode = HttpStatus.CONFLICT.value(),
            message = "Data integrity violation",
            data = errors.toMap()
        )
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response)
    }

    @ExceptionHandler(UsernameNotFoundException::class)
    fun handleUsernameNotFoundException(ex: UsernameNotFoundException): ResponseEntity<ApiResponse<ErrorDetails>> {
        val errorDetails = ErrorDetails(
            error = "Not Found",
            details = ex.message
        )
        val response = createApiResponse(
            statusCode = HttpStatus.NOT_FOUND.value(),
            message = "User not found",
            data = errorDetails
        )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ApiResponse<ErrorDetails>> {
        val errorDetails = ErrorDetails(
            error = "Internal server error",
            details = ex.message
        )
        val response = createApiResponse(
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            message = "Internal server error occurred",
            data = errorDetails
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