package com.ngacara.event.controllers

import com.ngacara.event.config.JwtService
import com.ngacara.event.helper.createApiResponse
import com.ngacara.event.models.*
import com.ngacara.event.services.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val userService: UserService,
    private val jwtService: JwtService
) {

    @PostMapping("/register")
    fun register(@Valid @RequestBody userRegistrationDto: UserRegistrationDto): ResponseEntity<ApiResponse<UserRegistrationDto>> {
        userService.register(userRegistrationDto)
        val response = createApiResponse(
            statusCode = HttpStatus.OK.value(),
            message = "User registered successfully",
            data = userRegistrationDto
        )
        return ResponseEntity.ok(response)
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody userLoginDto: UserLoginDto): ResponseEntity<ApiResponse<JwtResponse>> {
            val usernameOrEmail = userLoginDto.usernameOrEmail
            val user = userService.findByUsernameOrEmail(usernameOrEmail)
                ?: throw UsernameNotFoundException("Invalid username or email")

            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    usernameOrEmail, userLoginDto.password
                )
            )

            val token = jwtService.generateToken(user.username, user.id.toString())
            val response = createApiResponse(
                statusCode = HttpStatus.OK.value(),
                message = "Login successful",
                data = JwtResponse(token = token, userId = user.id.toString())
            )
         return ResponseEntity.ok(response)
    }

    @PostMapping("/logout")
    fun logout(@RequestHeader("Authorization") token: String): ResponseEntity<ApiResponse<Nothing>> {
        val jwtToken = token.removePrefix(prefix = "Bearer ")
        jwtService.blacklistToken(jwtToken)
        val response = createApiResponse(
            statusCode = HttpStatus.OK.value(),
            message = "Logged out successfully",
            data = null
        )
        return ResponseEntity.ok(response)
    }

    @PutMapping("/update/{userId}")
    fun updateUser(
        @PathVariable userId: UUID,
        @Valid @RequestBody updateDto: UserRegistrationDto
    ): ResponseEntity<ApiResponse<UserRegistrationDto>> {
        return try {
            userService.updateUser(userId, updateDto)
            val response = createApiResponse(
                statusCode = HttpStatus.OK.value(),
                message = "User updated successfully",
                data = updateDto
            )
            ResponseEntity.ok(response)
        } catch (e: IllegalArgumentException) {
            val response = createApiResponse(
                statusCode = HttpStatus.BAD_REQUEST.value(),
                message = e.message ?: "No changes detected",
                data = updateDto
            )
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
        }
    }

    @DeleteMapping("/delete/{userId}")
    fun deleteUser(@PathVariable userId: UUID): ResponseEntity<ApiResponse<Nothing>> {
        userService.deleteUser(userId)
        val response = createApiResponse(
            statusCode = HttpStatus.OK.value(),
            message = "User deleted successfully",
            data = null
        )
        return ResponseEntity.ok(response)
    }

    @GetMapping("/detail_user/{userId}")
    fun findUserById(@PathVariable userId: UUID): ResponseEntity<ApiResponse<User>> {
        val user = userService.findById(userId)
         val response = createApiResponse(
                statusCode = HttpStatus.OK.value(),
                message = "User found",
                data = user
            )
         return   ResponseEntity.ok(response)
    }

}
