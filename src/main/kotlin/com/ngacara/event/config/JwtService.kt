package com.ngacara.event.config

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Date
import java.security.Key

@Service
class JwtService {

    @Value("\${jwt.secret}")
    private lateinit var jwtSecret: String

    @Value("\${jwt.expiration}")
    private val expiration: Long = 3600

    private val blacklistedTokens = mutableSetOf<String>()
    private val secretKey: Key by lazy {
        Keys.hmacShaKeyFor(jwtSecret.toByteArray())
    }

    fun generateToken(username: String): String {
        return Jwts.builder()
            .setSubject(username)
            .setExpiration(Date(System.currentTimeMillis() + expiration * 1000))
            .signWith(secretKey)
            .compact()
    }

    fun getUsernameFromToken(token: String): String {
        val claims: Claims = getAllClaimsFromToken(token)
        return claims.subject
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)

            !isTokenExpired(token) && !blacklistedTokens.contains(token)
        } catch (e: Exception) {
            false
        }
    }

    fun blacklistToken(token: String) {
        blacklistedTokens.add(token)
    }

    private fun getAllClaimsFromToken(token: String): Claims {
        return try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).body
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid JWT token")
        }
    }

    private fun isTokenExpired(token: String): Boolean {
        val expiration: Date = getAllClaimsFromToken(token).expiration
        return expiration.before(Date())
    }
}