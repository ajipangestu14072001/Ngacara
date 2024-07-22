package com.ngacara.event.helper

import com.fasterxml.jackson.databind.ObjectMapper
import com.ngacara.event.models.ApiResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class CustomAuthenticationEntryPoint : AuthenticationEntryPoint {

    @Throws(IOException::class)
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        val apiResponse = ApiResponse(
            statusCode = HttpServletResponse.SC_UNAUTHORIZED,
            message = "Unauthorized: ${authException.message}",
            data = null
        )

        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = "application/json"
        response.writer.write(ObjectMapper().writeValueAsString(apiResponse))
    }
}
