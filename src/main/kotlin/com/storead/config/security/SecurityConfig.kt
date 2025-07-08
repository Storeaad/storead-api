package com.storead.config.security

import com.storead.auth.application.AuthService
import com.storead.auth.application.TokenService
import com.storead.config.security.endpoint.ApiAllowEndpoints
import com.storead.config.security.exceptions.EndpointAccessDeniedHandler
import com.storead.config.security.exceptions.EndpointAuthorizationHandler
import com.storead.config.security.jwt.JwtAuthenticationFilter
import com.storead.config.security.permission.PreAuthorizePermission
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.servlet.HandlerExceptionResolver
import org.springframework.web.servlet.handler.HandlerMappingIntrospector


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    private val tokenService: TokenService,
    private val authService: AuthService,
    private val handlerExceptionResolver: HandlerExceptionResolver,
    private val preAuthorizePermission: PreAuthorizePermission,
    private val allowEndpoints: ApiAllowEndpoints,
    private val accessDeniedHandler: EndpointAccessDeniedHandler,
    private val authorizationHandler: EndpointAuthorizationHandler,
) {

    private val ALLOW_ALL_URL: List<String> = listOf(
        "/h2-console/**",
        "/favicon.ico",
    )

    @Bean
    fun permitAllMatchers(introspector: HandlerMappingIntrospector): List<RequestMatcher> {
        val mvcMatcherBuilder = MvcRequestMatcher.Builder(introspector)
        return preAuthorizePermission.getAllowAnyEndpoints()
            .flatMap { (pattern, methods) ->
                // NOTE: 각 URL 패턴과 그에 해당하는 HTTP 메서드 목록 반복
                methods.map { method ->
                    // NOTE: MvcRequestMatcher.Builder에 HttpMethod와 URL 패턴을 함께 전달하여 정확한 조합의 RequestMatcher를 생성
                    mvcMatcherBuilder.pattern(HttpMethod.valueOf(method.name), pattern)
                }
            }
    }

    @Bean
    fun jwtAuthenticationFilter(
        permitAllMatchers: List<RequestMatcher>
    ): JwtAuthenticationFilter =
        JwtAuthenticationFilter(tokenService, authService, handlerExceptionResolver, permitAllMatchers, allowEndpoints)

    @Bean
    @Throws(Exception::class)
    fun filterChain(
        http: HttpSecurity,
        jwtAuthenticationFilter: JwtAuthenticationFilter,
        permitAllMatchers: List<RequestMatcher>
    ): SecurityFilterChain {

        val matchers = (ALLOW_ALL_URL.map { AntPathRequestMatcher(it) } + permitAllMatchers).toTypedArray()

        println("PermitAll URLs: " + matchers.joinToString { it.toString() })

        return http
            .cors { }
            .csrf { it.disable() }
            .headers { headers -> headers.frameOptions { it.disable() } }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers(*matchers).permitAll()
                    .anyRequest().authenticated()
            }
            .exceptionHandling {
                it.authenticationEntryPoint(authorizationHandler)
                    .accessDeniedHandler(accessDeniedHandler)
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }

    @ConditionalOnProperty(name = ["spring.h2.console.enabled"], havingValue = "true")
    fun configureH2ConsoleEnable(): WebSecurityCustomizer {
        return WebSecurityCustomizer { web: WebSecurity ->
            web.ignoring()
                .requestMatchers(PathRequest.toH2Console())
        }
    }


    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration().apply {
            allowedOrigins = listOf("https://example.com")
            allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
            allowedHeaders = listOf("*")
            allowCredentials = true
        }
        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", configuration)
        }
    }
}