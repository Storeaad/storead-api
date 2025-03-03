package com.storead.auth.client.auth.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.storead.auth.application.request.AuthServiceRequest
import com.storead.auth.domain.PlatformType


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class GoogleAccountResponse(
    val userId: String,
    val email: String
) {
    fun toServiceRequest(): AuthServiceRequest {
        return AuthServiceRequest(
            email.substringBefore("@"),
            email,
            userId,
            PlatformType.GOOGLE)
    }
}
