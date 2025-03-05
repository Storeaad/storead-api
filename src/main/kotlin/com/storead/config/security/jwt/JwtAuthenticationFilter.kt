package com.storead.config.security.jwt

import com.storead.auth.application.AuthService
import com.storead.auth.application.TokenService
import com.storead.auth.domain.User
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.util.AntPathMatcher
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
        if (ALLOW_ALL_URL.any { request.requestURI.startsWith(it) }) {
            filterChain.doFilter(request, response)
            return
        }
        val accessToken: String = resolveToken(request) ?: throw IllegalArgumentException("Access token not found")

        if (tokenService.validate(accessToken)) {
            val memberId: Long = tokenService.getSubject(accessToken)
            val user: User = authService.getUserById(memberId)
            val authenticationToken: Authentication = PreAuthenticatedAuthenticationToken(user, null, ArrayList())

            SecurityContextHolder.getContext().authentication = authenticationToken
        }

        filterChain.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        request.getHeader("Authorization")?.let {
            if (it.startsWith("Bearer ")) {
                return it.removePrefix("Bearer ")
            }
        }
        return null
    }
}