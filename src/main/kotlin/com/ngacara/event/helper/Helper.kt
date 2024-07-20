package com.ngacara.event.helper

import com.ngacara.event.models.ApiResponse

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
