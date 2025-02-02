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
    val role: Role = Role.USER,

    @Column(nullable = false, unique = true)
    val phoneNumber: String,

    @Column(nullable = false)
    var totalDonated: Double = 0.0,

    @Column
    var bankAccountId: UUID? = null,
)

enum class Role {
    USER,
    ADMIN
}

