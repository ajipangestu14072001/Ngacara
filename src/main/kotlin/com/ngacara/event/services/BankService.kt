package com.ngacara.event.services

import com.ngacara.event.helper.toPaginatedResponse
import com.ngacara.event.models.Bank
import com.ngacara.event.models.BankDto
import com.ngacara.event.models.BankResponseDto
import com.ngacara.event.models.PaginatedResponseDto
import com.ngacara.event.repository.BankRepository
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class BankService(private val bankRepository: BankRepository) {

    fun addBank(bankDto: BankDto): BankResponseDto {
        val bank = Bank(
            name = bankDto.name,
            adminFee = bankDto.adminFee,
            iconBank = bankDto.iconBank,
            swiftCode = bankDto.swiftCode
        )
        val savedBank = bankRepository.save(bank)

        return BankResponseDto(
            id = savedBank.id!!,
            name = savedBank.name,
            adminFee = savedBank.adminFee,
            iconBank = savedBank.iconBank,
            swiftCode = savedBank.swiftCode
        )
    }

    fun getAllBanks(page: Int, size: Int): PaginatedResponseDto<BankResponseDto> {
        val pageable = PageRequest.of(page, size)
        val banksPage = bankRepository.findAll(pageable)

        val bankResponseList = banksPage.content.map {
            BankResponseDto(
                id = it.id!!,
                name = it.name,
                adminFee = it.adminFee,
                iconBank = it.iconBank,
                swiftCode = it.swiftCode
            )
        }

        return toPaginatedResponse(PageImpl(bankResponseList, pageable, banksPage.totalElements))
    }
}

