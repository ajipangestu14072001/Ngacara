package com.ngacara.event.controllers

import com.ngacara.event.config.JwtService
import com.ngacara.event.models.ApiResponse
import com.ngacara.event.models.JwtResponse
import com.ngacara.event.models.UserLoginDto
import com.ngacara.event.models.UserRegistrationDto
import com.ngacara.event.services.UserService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val userService: UserService,
    private val jwtService: JwtService
) {

    @PostMapping("/register")
    fun register(@Valid @RequestBody userRegistrationDto: UserRegistrationDto): ResponseEntity<ApiResponse<Nothing>> {
        userService.register(userRegistrationDto)
        val response = ApiResponse(
            statusCode = 200,
            message = "User registered successfully",
            data = null
        )
        return ResponseEntity.ok(response)
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody userLoginDto: UserLoginDto): ResponseEntity<ApiResponse<JwtResponse>> {
        return try {
            val usernameOrEmail = userLoginDto.usernameOrEmail
            val user = userService.findByUsernameOrEmail(usernameOrEmail)
                ?: throw UsernameNotFoundException("Invalid username or email")

            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    usernameOrEmail, userLoginDto.password
                )
            )

            val token = jwtService.generateToken(user.username)
            val response = ApiResponse(
                statusCode = 200,
                message = "Login successful",
                data = JwtResponse(token)
            )
            ResponseEntity.ok(response)
        } catch (e: AuthenticationException) {
            val response = ApiResponse<JwtResponse>(
                statusCode = 401,
                message = "Invalid username or password",
                data = null
            )
            ResponseEntity.status(401).body(response)
        }
    }

    @PostMapping("/logout")
    fun logout(@RequestHeader("Authorization") token: String): ResponseEntity<ApiResponse<Nothing>> {
        val jwtToken = token.removePrefix("Bearer ")
        jwtService.blacklistToken(jwtToken)
        val response = ApiResponse(
            statusCode = 200,
            message = "Logged out successfully",
            data = null
        )
        return ResponseEntity.ok(response)
    }
}


