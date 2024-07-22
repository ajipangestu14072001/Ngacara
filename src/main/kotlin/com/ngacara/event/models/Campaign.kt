package com.ngacara.event.models

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "campaigns")
data class Campaign(
    @Id
    @GeneratedValue(generator = "UUID")
    val id: UUID? = null,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val description: String,

    @Column(nullable = false)
    val targetAmount: Double,

    @Column
    val photoPath: String? = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id", nullable = false)
    val createdBy: User
)

