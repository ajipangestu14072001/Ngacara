package com.ngacara.event.repository

import com.ngacara.event.models.BankAccount
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface BankAccountRepository : JpaRepository<BankAccount, UUID> {
    fun findByDonorId(donorId: UUID): List<BankAccount>
    fun findByCampaignId(campaignId: UUID): List<BankAccount>
}