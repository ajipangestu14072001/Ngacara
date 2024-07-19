package com.ngacara.event.models

import jakarta.validation.constraints.NotEmpty

data class UserRegistrationDto(
    @field:NotEmpty(message = "Username cannot be empty")
    val username: String,
    @field:NotEmpty(message = "Password cannot be empty")
    val password: String,
    val role: Role = Role.USER,
    val photoPath: String? = "",
    @field:NotEmpty(message = "Email cannot be empty")
    val email: String
)

data class UserLoginDto(
    @field:NotEmpty(message = "Username or email cannot be empty")
    val usernameOrEmail: String,
    @field:NotEmpty(message = "Password cannot be empty")
    val password: String
)


data class JwtResponse(
    val token: String
)

data class ApiResponse<T>(
    val statusCode: Int,
    val message: String,
    val data: T? = null
)
