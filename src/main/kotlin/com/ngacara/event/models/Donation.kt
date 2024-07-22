package com.ngacara.event.models

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "donations")
data class Donation(
    @Id
    @GeneratedValue(generator = "UUID")
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id", nullable = false)
    val donor: Donor,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", nullable = false)
    val campaign: Campaign,

    @Column(nullable = false)
    val amount: Double,

    @Column(nullable = false)
    val date: LocalDateTime = LocalDateTime.now()
)

