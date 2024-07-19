package com.ngacara.event.services

import com.ngacara.event.models.User
import com.ngacara.event.models.UserRegistrationDto
import com.ngacara.event.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun register(userRegistrationDto: UserRegistrationDto): User {
        val user = User(
            username = userRegistrationDto.username,
            password = passwordEncoder.encode(userRegistrationDto.password),
            email = userRegistrationDto.email,
            photoPath = userRegistrationDto.photoPath,
            role = userRegistrationDto.role
        )
        return userRepository.save(user)
    }

    fun findByUsernameOrEmail(usernameOrEmail: String): User? {
        return userRepository.findByUsername(usernameOrEmail)
            ?: userRepository.findByEmail(usernameOrEmail)
    }
}
