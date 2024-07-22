package com.ngacara.event.models

import java.util.UUID
import jakarta.persistence.*

@Entity
@Table(name = "bank_accounts")
data class BankAccount(
    @Id
    @GeneratedValue(generator = "UUID")
    val id: UUID? = null,

    @Column(nullable = false)
    val accountHolderName: String,

    @Column(nullable = false)
    val bankName: String,

    @Column(nullable = false, unique = true)
    val accountNumber: String,

    @Column
    val swiftCode: String? = null,

    @Column
    val iconBank: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val accountType: AccountType,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id")
    val donor: Donor? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id")
    val campaign: Campaign? = null,

)

enum class AccountType {
    DONOR,
    CAMPAIGN
}
