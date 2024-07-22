package com.ngacara.event.helper

import com.ngacara.event.models.ApiResponse
import com.ngacara.event.models.PaginatedResponseDto
import org.springframework.data.domain.Page

fun <T> createApiResponse(
    statusCode: Int,
    message: String,
    data: T? = null
): ApiResponse<T> {
    return ApiResponse(
        statusCode = statusCode,
        message = message,
        data = data
    )
}

fun <T> toPaginatedResponse(page: Page<T>): PaginatedResponseDto<T> {
    return PaginatedResponseDto(
        totalPages = page.totalPages,
        totalElements = page.totalElements,
        first = page.isFirst,
        last = page.isLast,
        size = page.size,
        content = page.content
    )
}

