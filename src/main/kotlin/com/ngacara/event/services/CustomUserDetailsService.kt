package com.ngacara.event.services

import com.ngacara.event.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(usernameOrEmail: String?): UserDetails {
        val user = usernameOrEmail?.let { userRepository.findByUsername(it) }
            ?: usernameOrEmail?.let { userRepository.findByEmail(it) }
            ?: throw UsernameNotFoundException("User not found")
        return org.springframework.security.core.userdetails.User(
            user.username, user.password, emptyList()
        )
    }
}
