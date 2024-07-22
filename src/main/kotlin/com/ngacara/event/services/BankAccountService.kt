package com.ngacara.event.services

import com.ngacara.event.helper.ResourceNotFoundException
import com.ngacara.event.models.*
import com.ngacara.event.repository.BankAccountRepository
import com.ngacara.event.repository.CampaignRepository
import com.ngacara.event.repository.DonorRepository
import com.ngacara.event.repository.UserRepository
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*

@Service
class BankAccountService(
    private val bankAccountRepository: BankAccountRepository,
    private val donorRepository: DonorRepository,
    private val campaignRepository: CampaignRepository,
    private val userRepository: UserRepository
) {
    private fun validateExistence(
        accountType: AccountType,
        donorId: UUID?,
        campaignId: UUID?
    ) {
        if (accountType == AccountType.DONOR && (donorId == null || !donorRepository.existsById(donorId))) {
            throw ResourceNotFoundException("Donor dengan ID $donorId tidak ditemukan")
        }

        if (accountType == AccountType.CAMPAIGN && (campaignId == null || !campaignRepository.existsById(campaignId))) {
            throw ResourceNotFoundException("Kampanye dengan ID $campaignId tidak ditemukan")
        }
    }

    private fun getAssociatedEntity(
        accountType: AccountType,
        donorId: UUID?,
        campaignId: UUID?
    ): Pair<Donor?, Campaign?> {
        val donor = if (accountType == AccountType.DONOR) donorId?.let { donorRepository.findById(it).orElse(null) } else null
        val campaign = if (accountType == AccountType.CAMPAIGN) campaignId?.let { campaignRepository.findById(it).orElse(null) } else null
        return Pair(donor, campaign)
    }

    fun addBankAccount(bankAccountDto: BankAccountDto): BankAccountResponseDto {
        validateExistence(bankAccountDto.accountType, bankAccountDto.donorId, bankAccountDto.campaignId)
        val (donor, campaign) = getAssociatedEntity(bankAccountDto.accountType, bankAccountDto.donorId, bankAccountDto.campaignId)

        donor?.let {
            if (it.bankAccount != null) {
                throw IllegalArgumentException("Donor dengan ID ${it.id} sudah memiliki akun bank")
            }
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
        val savedBankAccount = bankAccountRepository.save(bankAccount)

        donor?.let {
            val updatedDonor = it.copy(bankAccount = savedBankAccount)
            donorRepository.save(updatedDonor)
            updateUserBankAccountId(it.user.id!!, savedBankAccount.id!!)
        }

        return BankAccountResponseDto(
            id = savedBankAccount.id!!,
            accountHolderName = savedBankAccount.accountHolderName,
            bankName = savedBankAccount.bankName,
            accountNumber = savedBankAccount.accountNumber,
            swiftCode = savedBankAccount.swiftCode,
            accountType = savedBankAccount.accountType,
            iconBank = savedBankAccount.iconBank,
            donorId = donor?.id,
            campaignId = campaign?.id
        )
    }

    private fun updateUserBankAccountId(userId: UUID, bankAccountId: UUID) {
        val user = userRepository.findById(userId)
            .orElseThrow { UsernameNotFoundException("User dengan ID $userId tidak ditemukan") }
        user.bankAccountId = bankAccountId
        userRepository.save(user)
    }
}
