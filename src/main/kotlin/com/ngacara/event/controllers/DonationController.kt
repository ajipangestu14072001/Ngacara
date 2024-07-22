package com.ngacara.event.controllers

import com.ngacara.event.helper.createApiResponse
import com.ngacara.event.models.*
import com.ngacara.event.services.DonationService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/donations")
class DonationController(
    private val donationService: DonationService
) {

    @PostMapping("/donors")
    fun createDonor(@Valid @RequestBody donorDto: DonorDto): ResponseEntity<ApiResponse<Donor>> {
        val donor = donationService.createDonor(donorDto)
        val response = createApiResponse(
            statusCode = HttpStatus.OK.value(),
            message = "Donor created successfully",
            data = donor
        )
        return ResponseEntity.ok(response)
    }

    @PostMapping("/campaigns")
    fun createCampaign(@Valid @RequestBody campaignDto: CampaignDto): ResponseEntity<ApiResponse<CampaignResponseDto>> {
        val campaign = donationService.createCampaign(campaignDto)

        val campaignResponseDto = CampaignResponseDto(
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

        val response = createApiResponse(
            statusCode = HttpStatus.OK.value(),
            message = "Campaign created successfully",
            data = campaignResponseDto
        )
        return ResponseEntity.ok(response)
    }

    @PostMapping
    fun createDonation(@Valid @RequestBody donationDto: DonationDto): ResponseEntity<ApiResponse<DonationResponseDto>> {
        val donation = donationService.createDonation(donationDto)
        val response = createApiResponse(
            statusCode = HttpStatus.OK.value(),
            message = "Donation created successfully",
            data = donation
        )
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{campaignId}")
    fun getDonationsByCampaign(
        @PathVariable campaignId: UUID,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<ApiResponse<PaginatedResponseDto<DonationResponseDto>>> {
        val paginatedDonations = donationService.getDonationsByCampaign(campaignId, page, size)
        return ResponseEntity.ok(
            createApiResponse(
                statusCode = HttpStatus.OK.value(),
                message = "Donations retrieved successfully",
                data = paginatedDonations
            )
        )
    }

    @GetMapping("/campaigns")
    fun getAllCampaigns(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<ApiResponse<PaginatedResponseDto<CampaignResponseDto>>> {
        val paginatedCampaigns = donationService.getAllCampaigns(page, size)
        return ResponseEntity.ok(
            createApiResponse(
                statusCode = HttpStatus.OK.value(),
                message = "Campaigns retrieved successfully",
                data = paginatedCampaigns
            )
        )
    }

    @GetMapping("/campaigns/{campaignId}")
    fun getCampaignById(@PathVariable campaignId: UUID): ResponseEntity<ApiResponse<CampaignResponseDto>> {
        val campaign = donationService.getCampaignById(campaignId)
        val response = createApiResponse(
            statusCode = HttpStatus.OK.value(),
            message = "Campaign retrieved successfully",
            data = campaign
        )
        return ResponseEntity.ok(response)
    }
}
