package com.ngacara.event.repository

import com.ngacara.event.models.Donor
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface DonorRepository : JpaRepository<Donor, UUID> {
    fun existsByEmailAndUserId(email: String, userId: UUID): Boolean
    fun existsByPhoneNumberAndUserId(phoneNumber: String, userId: UUID): Boolean
}
