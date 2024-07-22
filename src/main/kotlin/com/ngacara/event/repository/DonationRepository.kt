package com.ngacara.event.repository

import com.ngacara.event.models.Donation
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface DonationRepository : JpaRepository<Donation, UUID>{
    fun findByCampaignId(campaignId: UUID, pageable: Pageable): Page<Donation>
}