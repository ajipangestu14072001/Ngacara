package com.ngacara.event.models

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "banks")
data class Bank(
    @Id
    @GeneratedValue(generator = "UUID")
    val id: UUID? = null,

    @Column(nullable = false, unique = true)
    val name: String,

    @Column(nullable = false)
    val adminFee: Double,

    @Column
    val iconBank: String? = null,

    @Column
    val swiftCode: String? = null
)
