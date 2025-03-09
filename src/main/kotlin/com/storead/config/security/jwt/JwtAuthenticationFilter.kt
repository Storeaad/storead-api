package com.storead.config.security.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import com.storead.auth.application.AuthService
import com.storead.auth.application.TokenService
import com.storead.auth.domain.User
import com.storead.common.constants.Headers
import com.storead.common.web.ApiResponse
import com.storead.config.security.jwt.exceptions.JwtAuthenticationException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val tokenService: TokenService,
    private val authService: AuthService
) : OncePerRequestFilter() {

    private val ALLOW_ALL_URL: List<String> = listOf(
        "/api/v1/auth",
        "/h2-console",
        "/favicon.ico",
    )

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        try {
            if (ALLOW_ALL_URL.any { request.requestURI.startsWith(it) }) {
                filterChain.doFilter(request, response)
                return
            }

            val accessToken: String = resolveToken(request)
                ?: throw JwtAuthenticationException("액세스 토큰을 찾을 수 없습니다.")

            if (tokenService.validate(accessToken)) {
                val memberId: Long = tokenService.getSubject(accessToken)
                val user: User = authService.getUserById(memberId)
                val authenticationToken: Authentication = PreAuthenticatedAuthenticationToken(user, null, ArrayList())

                SecurityContextHolder.getContext().authentication = authenticationToken

                filterChain.doFilter(request, response)
            }

            throw JwtAuthenticationException("잘못된 토큰 정보입니다.")

        } catch (e: JwtAuthenticationException) {
            SecurityContextHolder.clearContext()

            response.contentType = "application/json;charset=UTF-8"
            response.status = e.status.value()

            val apiResponse = ApiResponse(
                data = "",
                message = e.message,
                status = e.status
            )

            val objectMapper = ObjectMapper()
            response.writer.write(objectMapper.writeValueAsString(apiResponse))
            response.writer.flush()

        }
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        request.getHeader(Headers.AUTHORIZATION)?.let {
            if (it.startsWith(Headers.BEARER_NEXT_SPACE)) {
                return it.removePrefix(Headers.BEARER_NEXT_SPACE)
            }
        }
        return null
    }
}