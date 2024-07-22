package com.ngacara.event.repository

import com.ngacara.event.models.Bank
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface BankRepository : JpaRepository<Bank, UUID>
