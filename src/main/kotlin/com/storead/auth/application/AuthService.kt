package com.storead.auth.application

import com.storead.auth.application.request.AuthServiceRequest
import com.storead.auth.application.response.AuthServiceResponse
import com.storead.auth.domain.AuthRepository
import com.storead.auth.domain.User
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val authRepository: AuthRepository,
    private val tokenService: TokenService,
) {

    fun login(serviceRequest: AuthServiceRequest): AuthServiceResponse {
        val user: User =
            authRepository.findByPlatformIdAndPlatform(serviceRequest.platformId, serviceRequest.platform) ?: saveUser(
                serviceRequest
            )

        return AuthServiceResponse(
            tokenService.createAccessToken(user),
            tokenService.createRefreshToken(user),
            user.name,
        )
    }

    fun logout(token: String) {
        tokenService.delete(token.removePrefix("Bearer "))
    }

    fun getUserById(userId: Long): User =
        authRepository.findByIdOrNull(userId) ?: throw IllegalArgumentException("유저를 찾을 수 없습니다.")

    private fun saveUser(request: AuthServiceRequest): User {
        return authRepository.save(request.toEntity())
    }
}