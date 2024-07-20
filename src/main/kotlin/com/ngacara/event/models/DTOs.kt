package com.ngacara.event.models

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern
import java.util.*

data class UserRegistrationDto(
    @field:NotEmpty(message = "Username cannot be empty")
    val username: String,
    @field:NotEmpty(message = "Password cannot be empty")
    val password: String,
    val role: Role = Role.USER,
    val photoPath: String? = "",
    @field:NotEmpty(message = "Email cannot be empty")
    @field:Email(message = "Email should be valid")
    val email: String,
    @field:NotEmpty(message = "Phone number cannot be empty")
    @field:Pattern(regexp = "^\\+62[0-9]{9,15}$", message = "Phone number should be valid and start with +62")
    val phoneNumber: String
)

data class UserLoginDto(
    @field:NotEmpty(message = "Username or email cannot be empty")
    val usernameOrEmail: String,
    @field:NotEmpty(message = "Password cannot be empty")
    val password: String
)


data class JwtResponse(
    val token: String? = "",
    val userId: String? = "",
    val error: String? = ""
)

data class ApiResponse<T>(
    val statusCode: Int,
    val message: String,
    val data: T? = null
)

data class ErrorDetails(
    val error: String,
    val details: String?
)
