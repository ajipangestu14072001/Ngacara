package com.ngacara.event.controllers

import com.ngacara.event.helper.createApiResponse
import com.ngacara.event.models.ApiResponse
import com.ngacara.event.models.BankDto
import com.ngacara.event.models.BankResponseDto
import com.ngacara.event.models.PaginatedResponseDto
import com.ngacara.event.services.BankService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/banks")
class BankController(private val bankService: BankService) {

    @PostMapping
    fun addBank(@Valid @RequestBody bankDto: BankDto): ResponseEntity<ApiResponse<BankResponseDto>> {
        val bankResponse = bankService.addBank(bankDto)
        return ResponseEntity.ok(
            createApiResponse(
                statusCode = HttpStatus.OK.value(),
                message = "Bank added successfully",
                data = bankResponse
            )
        )
    }

    @GetMapping
    fun getAllBanks(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<ApiResponse<PaginatedResponseDto<BankResponseDto>>> {
        val paginatedBanks = bankService.getAllBanks(page, size)
        return ResponseEntity.ok(
            createApiResponse(
                statusCode = HttpStatus.OK.value(),
                message = "Banks retrieved successfully",
                data = paginatedBanks
            )
        )
    }
}


