package com.ngacara.event.services

import com.ngacara.event.models.User
import com.ngacara.event.models.UserRegistrationDto
import com.ngacara.event.repository.UserRepository
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

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
            role = userRegistrationDto.role,
            phoneNumber = userRegistrationDto.phoneNumber
        )
        return userRepository.save(user)
    }

    fun findByUsernameOrEmail(usernameOrEmail: String): User? {
        return userRepository.findByUsername(usernameOrEmail)
            ?: userRepository.findByEmail(usernameOrEmail)
    }

    fun updateUser(userId: UUID, updateDto: UserRegistrationDto): User {
        val existingUser = userRepository.findById(userId).orElseThrow {
            UsernameNotFoundException("User not found")
        }

        val hasChanges = existingUser.username != updateDto.username ||
                existingUser.email != updateDto.email ||
                existingUser.phoneNumber != updateDto.phoneNumber ||
                existingUser.photoPath != updateDto.photoPath ||
                !passwordEncoder.matches(updateDto.password, existingUser.password)

        if (!hasChanges) {
            throw IllegalArgumentException("No changes detected")
        }

        val updatedUser = existingUser.copy(
            username = updateDto.username,
            password = passwordEncoder.encode(updateDto.password),
            email = updateDto.email,
            photoPath = updateDto.photoPath,
            role = updateDto.role,
            phoneNumber = updateDto.phoneNumber
        )
        return userRepository.save(updatedUser)
    }

    fun findById(userId: UUID): User {
        return userRepository.findById(userId).orElseThrow {
            throw UsernameNotFoundException("User not found")
        }
    }

    fun deleteUser(userId: UUID) {
        if (!userRepository.existsById(userId)) {
            throw UsernameNotFoundException("User not found")
        }
        userRepository.deleteById(userId)
    }
}

