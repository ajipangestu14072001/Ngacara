package com.ngacara.event.models

import jakarta.validation.constraints.*
import java.util.*

data class UserRegistrationDto(
    @field:NotEmpty(message = "Username cannot be empty")
    val username: String,
    @field:NotEmpty(message = "Password cannot be empty")
    val password: String,
    val role: Role = Role.USER,
    val photoPath: String? = "",
    @field:NotEmpty(message = "Email cannot be empty")
    @field:Email(message = "Email should be valid")
    val email: String,
    @field:NotEmpty(message = "Phone number cannot be empty")
    @field:Pattern(regexp = "^\\+62[0-9]{9,15}$", message = "Phone number should be valid and start with +62")
    val phoneNumber: String
)

data class UserResponseDto(
    val username: String,
    val email: String,
    val photoPath: String?,
    val phoneNumber: String
)

data class UserLoginDto(
    @field:NotEmpty(message = "Username or email cannot be empty")
    val usernameOrEmail: String,
    @field:NotEmpty(message = "Password cannot be empty")
    val password: String
)


data class JwtResponse(
    val token: String? = "",
    val userId: String? = ""
)

data class ApiResponse<T>(
    val statusCode: Int,
    val message: String,
    val data: T? = null
)

data class PaginatedResponseDto<T>(
    val totalPages: Int,
    val totalElements: Long,
    val first: Boolean,
    val last: Boolean,
    val size: Int,
    val content: List<T>
)

data class CampaignDto(
    @field:NotEmpty(message = "Name cannot be empty")
    val name: String,

    @field:NotEmpty(message = "Description cannot be empty")
    val description: String,

    @field:NotNull(message = "Target amount cannot be null")
    @field:Positive(message = "Target amount must be positive")
    val targetAmount: Double,

    val photoPath: String? = "",

    @field:NotNull(message = "Created by user ID cannot be empty")
    val createdByUserId: UUID
)

data class CampaignResponseDto(
    val id: UUID,
    val name: String,
    val description: String,
    val targetAmount: Double,
    val photoPath: String,
    val createdBy: UserResponseDto
)

data class DonationDto(
    @field:NotNull(message = "Donor ID cannot be empty")
    val donorId: UUID,

    @field:NotNull(message = "Campaign ID cannot be empty")
    val campaignId: UUID,

    @field:NotNull(message = "User ID cannot be empty")
    val userId: UUID,

    @field:NotNull(message = "Amount cannot be null")
    @field:Positive(message = "Amount must be positive")
    val amount: Double
)

data class DonorResponseDto(
    val name: String,
    val email: String,
    val phoneNumber: String
)

data class DonationResponseDto(
    val donor: DonorResponseDto,
    val campaign: CampaignResponseDto,
    val user: UserResponseDto,
    val amount: Double
)

data class BankAccountDto(
    val id: UUID,
    val accountHolderName: String,
    val bankName: String,
    val accountNumber: String,
    val swiftCode: String?,
    val iconBank: String? = null,
    val accountType: AccountType,
    val donorId: UUID? = null,
    val campaignId: UUID? = null
)

data class BankAccountResponseDto(
    val id: UUID,
    val accountHolderName: String,
    val bankName: String,
    val accountNumber: String,
    val swiftCode: String?,
    val accountType: AccountType,
    val donorId: UUID? = null,
    val iconBank: String? = null,
    val campaignId: UUID? = null
)

data class BankDto(
    val id: UUID? = null,
    val name: String,
    val adminFee: Double,
    val iconBank: String? = null,
    val swiftCode: String? = null
)

data class BankResponseDto(
    val id: UUID,
    val name: String,
    val adminFee: Double,
    val iconBank: String? = null,
    val swiftCode: String? = null
)

data class DonorDto(
    @field:NotEmpty(message = "Name cannot be empty")
    val name: String,
    @field:NotEmpty(message = "Email cannot be empty")
    @field:Email(message = "Email should be valid")
    val email: String,
    @field:NotEmpty(message = "Phone number cannot be empty")
    @field:Pattern(regexp = "^\\+62[0-9]{9,15}$", message = "Phone number should be valid and start with +62")
    val phoneNumber: String
)

