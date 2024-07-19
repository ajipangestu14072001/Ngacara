package com.ngacara.event.models

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(generator = "UUID")
    val id: UUID? = null,

    @Column(nullable = false, unique = true)
    val username: String,

    @Column(nullable = false)
    var password: String,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column
    var photoPath: String? = "",

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val role: Role = Role.USER
)

enum class Role {
    USER,
    ADMIN
}

