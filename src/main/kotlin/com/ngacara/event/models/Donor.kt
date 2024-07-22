package com.ngacara.event.models

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "donors")
data class Donor(
    @Id
    @GeneratedValue(generator = "UUID")
    val id: UUID? = null,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false, unique = true)
    val phoneNumber: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_account_id")
    val bankAccount: BankAccount? = null

)

