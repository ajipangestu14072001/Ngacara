package com.ngacara.event.services

import com.ngacara.event.models.*
import com.ngacara.event.repository.BankAccountRepository
import com.ngacara.event.repository.CampaignRepository
import com.ngacara.event.repository.DonorRepository
import org.springframework.stereotype.Service

@Service
class BankAccountService(
    private val bankAccountRepository: BankAccountRepository,
    private val donorRepository: DonorRepository,
    private val campaignRepository: CampaignRepository
) {
    fun addBankAccount(bankAccountDto: BankAccountDto): BankAccountResponseDto {
        val donor: Donor? = if (bankAccountDto.accountType == AccountType.DONOR) {
            bankAccountDto.donorId?.let {
                donorRepository.findById(it).orElse(null)
            }
        } else {
            null
        }

        val campaign: Campaign? = if (bankAccountDto.accountType == AccountType.CAMPAIGN) {
            bankAccountDto.campaignId?.let {
                campaignRepository.findById(it).orElse(null)
            }
        } else {
            null
        }

        val bankAccount = BankAccount(
            accountHolderName = bankAccountDto.accountHolderName,
            bankName = bankAccountDto.bankName,
            accountNumber = bankAccountDto.accountNumber,
            swiftCode = bankAccountDto.swiftCode,
            iconBank = bankAccountDto.iconBank,
            accountType = bankAccountDto.accountType,
            donor = donor,
            campaign = campaign
        )
        bankAccountRepository.save(bankAccount)

        return BankAccountResponseDto(
            id = bankAccount.id!!,
            accountHolderName = bankAccount.accountHolderName,
            bankName = bankAccount.bankName,
            accountNumber = bankAccount.accountNumber,
            swiftCode = bankAccount.swiftCode,
            accountType = bankAccount.accountType,
            iconBank = bankAccount.iconBank,
            donorId = donor?.id,
            campaignId = campaign?.id
        )
    }
}


