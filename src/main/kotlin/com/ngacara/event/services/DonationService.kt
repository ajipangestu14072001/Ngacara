package com.ngacara.event.services

import com.ngacara.event.helper.ResourceNotFoundException
import com.ngacara.event.helper.toPaginatedResponse
import com.ngacara.event.models.*
import com.ngacara.event.repository.CampaignRepository
import com.ngacara.event.repository.DonationRepository
import com.ngacara.event.repository.DonorRepository
import com.ngacara.event.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*

@Service
class DonationService(
    private val donorRepository: DonorRepository,
    private val campaignRepository: CampaignRepository,
    private val donationRepository: DonationRepository,
    private val userRepository: UserRepository
) {

    @Transactional
    fun createDonor(donorDto: DonorDto): DonorCreateResponseDto {
        val user = userRepository.findById(donorDto.userId)
            .orElseThrow { ResourceNotFoundException("Data not found") }

        if (donorRepository.existsByEmailAndUserId(donorDto.email, donorDto.userId)) {
            throw IllegalArgumentException("Donor dengan email ${donorDto.email} sudah ada untuk pengguna ini")
        }
        if (donorRepository.existsByPhoneNumberAndUserId(donorDto.phoneNumber, donorDto.userId)) {
            throw IllegalArgumentException("Donor dengan nomor telepon ${donorDto.phoneNumber} sudah ada untuk pengguna ini")
        }
        val donor = Donor(
            name = donorDto.name,
            email = donorDto.email,
            phoneNumber = donorDto.phoneNumber,
            user = user
        )
        val savedDonor = donorRepository.save(donor)

        return DonorCreateResponseDto(
            name = savedDonor.name,
            email = savedDonor.email,
            phoneNumber = savedDonor.phoneNumber,
            user = UserResponseDto(
                username = savedDonor.user.username,
                email = savedDonor.user.email,
                photoPath = savedDonor.user.photoPath,
                phoneNumber = savedDonor.user.phoneNumber,
            )
        )
    }

    @Transactional
    fun createCampaign(campaignDto: CampaignDto): Campaign {
        val user = userRepository.findById(campaignDto.createdByUserId)
            .orElseThrow { UsernameNotFoundException("User not found") }
        val campaign = Campaign(
            name = campaignDto.name,
            description = campaignDto.description,
            targetAmount = campaignDto.targetAmount,
            photoPath = campaignDto.photoPath,
            createdBy = user
        )
        return campaignRepository.save(campaign)
    }

    @Transactional
    fun createDonation(donationDto: DonationDto): DonationResponseDto {
        val donor = donorRepository.findById(donationDto.donorId)
            .orElseThrow { ResourceNotFoundException("Donor not found") }
        val campaign = campaignRepository.findById(donationDto.campaignId)
            .orElseThrow { ResourceNotFoundException("Campaign not found") }
        val user = userRepository.findById(donationDto.userId)
            .orElseThrow { UsernameNotFoundException("User not found") }

        val donation = Donation(
            donor = donor,
            campaign = campaign,
            user = user,
            amount = donationDto.amount
        )
        donationRepository.save(donation)
        user.totalDonated += donationDto.amount
        userRepository.save(user)

        return DonationResponseDto(
            donor = DonorResponseDto(
                name = donor.name,
                email = donor.email,
                phoneNumber = donor.phoneNumber
            ),
            campaign = CampaignResponseDto(
                id = campaign.id!!,
                name = campaign.name,
                description = campaign.description,
                targetAmount = campaign.targetAmount,
                photoPath = campaign.photoPath ?: "",
                createdBy = UserResponseDto(
                    username = campaign.createdBy.username,
                    email = campaign.createdBy.email,
                    photoPath = campaign.createdBy.photoPath,
                    phoneNumber = campaign.createdBy.phoneNumber
                )
            ),
            user = UserResponseDto(
                username = user.username,
                email = user.email,
                photoPath = user.photoPath,
                phoneNumber = user.phoneNumber
            ),
            amount = donation.amount
        )
    }

    fun getDonationsByCampaign(campaignId: UUID, page: Int, size: Int): PaginatedResponseDto<DonationResponseDto> {
        val pageable = PageRequest.of(page, size)
        val donationsPage = donationRepository.findByCampaignId(campaignId, pageable)

        val donationResponseList = donationsPage.content.map {
            DonationResponseDto(
                donor = DonorResponseDto(
                    name = it.donor.name,
                    email = it.donor.email,
                    phoneNumber = it.donor.phoneNumber
                ),
                campaign = CampaignResponseDto(
                    id = it.campaign.id!!,
                    name = it.campaign.name,
                    description = it.campaign.description,
                    targetAmount = it.campaign.targetAmount,
                    photoPath = it.campaign.photoPath ?: "",
                    createdBy = UserResponseDto(
                        username = it.campaign.createdBy.username,
                        email = it.campaign.createdBy.email,
                        photoPath = it.campaign.createdBy.photoPath,
                        phoneNumber = it.campaign.createdBy.phoneNumber
                    )
                ),
                user = UserResponseDto(
                    username = it.user.username,
                    email = it.user.email,
                    photoPath = it.user.photoPath,
                    phoneNumber = it.user.phoneNumber
                ),
                amount = it.amount
            )
        }

        return toPaginatedResponse(PageImpl(donationResponseList, pageable, donationsPage.totalElements))
    }

    fun getAllCampaigns(page: Int, size: Int): PaginatedResponseDto<CampaignResponseDto> {
        val pageable = PageRequest.of(page, size)
        val campaignsPage = campaignRepository.findAll(pageable)

        val campaignResponseList = campaignsPage.content.map {
            CampaignResponseDto(
                id = it.id!!,
                name = it.name,
                description = it.description,
                targetAmount = it.targetAmount,
                photoPath = it.photoPath ?: "",
                createdBy = UserResponseDto(
                    username = it.createdBy.username,
                    email = it.createdBy.email,
                    photoPath = it.createdBy.photoPath,
                    phoneNumber = it.createdBy.phoneNumber
                )
            )
        }

        return toPaginatedResponse(PageImpl(campaignResponseList, pageable, campaignsPage.totalElements))
    }

    fun getCampaignById(campaignId: UUID): CampaignResponseDto {
        val campaign = campaignRepository.findById(campaignId)
            .orElseThrow { IllegalArgumentException("Campaign not found") }

        return toCampaignResponseDto(campaign)
    }

    fun getDonorById(donorId: UUID): DonorResponseDto {
        val donor = donorRepository.findById(donorId)
            .orElseThrow {
                throw ResourceNotFoundException("Data not found")
            }

        return DonorResponseDto(
            name = donor.name,
            email = donor.email,
            phoneNumber = donor.phoneNumber,
            bankAccount = donor.bankAccount?.id
        )
    }

    private fun toCampaignResponseDto(campaign: Campaign): CampaignResponseDto {
        return CampaignResponseDto(
            id = campaign.id!!,
            name = campaign.name,
            description = campaign.description,
            targetAmount = campaign.targetAmount,
            photoPath = campaign.photoPath ?: "",
            createdBy = UserResponseDto(
                username = campaign.createdBy.username,
                email = campaign.createdBy.email,
                photoPath = campaign.createdBy.photoPath,
                phoneNumber = campaign.createdBy.phoneNumber
            )
        )
    }
}

