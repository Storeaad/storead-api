package com.storead.config.security.jwt

import com.storead.auth.application.AuthService
import com.storead.auth.application.TokenService
import com.storead.auth.domain.User
import com.storead.common.constants.Headers
import com.storead.config.security.endpoint.ApiAllowEndpoints
import com.storead.config.security.exceptions.JwtAuthenticationException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver
import java.util.*

class JwtAuthenticationFilter(
    private val tokenService: TokenService,
    private val authService: AuthService,
    private val handlerExceptionResolver: HandlerExceptionResolver,
    private val permitAllMatchers: List<RequestMatcher>,
    private val allowEndpoints: ApiAllowEndpoints,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {

            if (isPermitAllRequest(request) || doesNotAllowEndpoint(request)) {
                filterChain.doFilter(request, response)
                return
            }

            val accessToken: String = resolveToken(request) ?: throw JwtAuthenticationException("액세스 토큰을 찾을 수 없습니다.")

            if (tokenService.validate(accessToken)) {
                val memberId: UUID = tokenService.getSubject(accessToken)
                val user: User = authService.getUserById(memberId)
                val authenticationToken: Authentication = PreAuthenticatedAuthenticationToken(user, null, ArrayList())

                SecurityContextHolder.getContext().authentication = authenticationToken

                filterChain.doFilter(request, response)
                return
            }

            throw JwtAuthenticationException("잘못된 토큰 정보입니다.")

        } catch (exception: Exception) {
            SecurityContextHolder.clearContext()
            handlerExceptionResolver.resolveException(request, response, null, exception)
        }
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        return request.getHeader(Headers.AUTHORIZATION)?.let {
            if (it.startsWith(Headers.BEARER_NEXT_SPACE)) {
                it.removePrefix(Headers.BEARER_NEXT_SPACE)
            } else null
        }
    }

    private fun doesNotAllowEndpoint(request: HttpServletRequest): Boolean {
        return !allowEndpoints.matches(request)
    }

    private fun isPermitAllRequest(request: HttpServletRequest): Boolean {
        return permitAllMatchers.any { it.matches(request) }
    }
}