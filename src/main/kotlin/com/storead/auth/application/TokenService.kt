package com.storead.auth.application

import com.storead.auth.application.request.TokenServiceRequest
import com.storead.auth.application.response.TokenServiceResponse
import com.storead.auth.domain.AuthRepository
import com.storead.auth.domain.RefreshToken
import com.storead.auth.domain.RefreshTokenRepository
import com.storead.auth.domain.User
import com.storead.auth.exception.AuthException
import com.storead.config.properties.JwtProperties
import io.jsonwebtoken.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*


@Component
class TokenService(

    private val jwtProperties: JwtProperties,
    private val tokenRepository: RefreshTokenRepository,
    private val authRepository: AuthRepository,
) {

    fun createAccessToken(payload: User, expired: Date? = null): String = Jwts.builder()
        .subject(payload.id.toString())
        .issuedAt(Date())
        .expiration(
            expired ?: Date.from(
                Instant.now().plus(jwtProperties.accessTokenLifetimeMinutes, ChronoUnit.MINUTES)
            )
        )
        .signWith(jwtProperties.secretKey)
        .compact()

    fun createRefreshToken(payload: User): String {
        val refreshToken: String = Jwts.builder()
            .issuedAt(Date())
            .expiration(Date.from(Instant.now().plus(jwtProperties.refreshTokenLifetimeDays, ChronoUnit.DAYS)))
            .signWith(jwtProperties.secretKey)
            .compact()

        tokenRepository.save(RefreshToken(payload.id, refreshToken))
        return refreshToken
    }

    fun reIssue(request: TokenServiceRequest): TokenServiceResponse {
        val userId: Long = getSubject(request.accessToken)
        val user: User = authRepository.findByIdOrNull(userId) ?: throw AuthException("유저를 찾을 수 없습니다.")
        val refreshToken: RefreshToken =
            tokenRepository.findByUserId(userId) ?: throw AuthException("토큰이 만료 되었습니다.")

        require(refreshToken.validate(request.refreshToken))

        val newRefreshToken = createRefreshToken(user)
        val newAccessToken = createAccessToken(user)

        tokenRepository.save(refreshToken.update(newRefreshToken))

        return TokenServiceResponse(newAccessToken, newRefreshToken)
    }

    fun delete(accessToken: String) {
        val userId: Long = getSubject(accessToken)
        tokenRepository.deleteById(userId.toString())
    }

    /**
     * TODO:
     *  1. println -> Logging
     */
    fun validate(token: String): Boolean {
        return try {
            val claims = parseClaims(token)
            println("claim: $claims")
            !claims.payload.expiration.before(Date())
        } catch (e: JwtException) {
            println("jwt Exception: ${e.message}")
            false
        } catch (e: IllegalArgumentException) {
            println("Illegal: ${e.message}")
            false
        }
    }

    fun getSubject(token: String): Long {
        return try {
            val claims = parseClaims(token)
            claims.payload.subject.toLong()
        } catch (e: ExpiredJwtException) {
            e.claims.subject.toLong()
        }
    }

    private fun parseClaims(token: String): Jws<Claims> = Jwts.parser()
        .verifyWith(jwtProperties.secretKey)
        .build()
        .parseSignedClaims(token)
}