package com.ngacara.event.controllers

import com.ngacara.event.helper.createApiResponse
import com.ngacara.event.models.ApiResponse
import com.ngacara.event.models.BankAccountDto
import com.ngacara.event.models.BankAccountResponseDto
import com.ngacara.event.services.BankAccountService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/bank-accounts")
class BankAccountController(
    private val bankAccountService: BankAccountService
) {
    @PostMapping
    fun addBankAccount(
        @Valid @RequestBody bankAccountDto: BankAccountDto
    ): ResponseEntity<ApiResponse<BankAccountResponseDto>> {
        val bankAccountResponseDto = bankAccountService.addBankAccount(bankAccountDto)
        val response = createApiResponse(
            statusCode = HttpStatus.CREATED.value(),
            message = "Bank account added successfully",
            data = bankAccountResponseDto
        )
        return ResponseEntity.ok(response)
    }
}
