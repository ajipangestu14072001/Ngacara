package com.ngacara.event.repository

import com.ngacara.event.models.Campaign
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CampaignRepository : JpaRepository<Campaign, UUID>
