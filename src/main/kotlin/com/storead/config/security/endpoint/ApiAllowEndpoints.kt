package com.storead.config.security.endpoint

import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.ApplicationContext
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.stereotype.Component
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping

@Component
class ApiAllowEndpoints(
    private val applicationContext: ApplicationContext
) {

    private val allEndpointsMatcher: RequestMatcher

    init {
        val requestMappingHandlerMapping = applicationContext.getBean(
            "requestMappingHandlerMapping",
            RequestMappingHandlerMapping::class.java
        )

        val matchers = requestMappingHandlerMapping.handlerMethods.keys.flatMap { mappingInfo ->
            val httpMethods = mappingInfo.methodsCondition.methods
            val patterns = mappingInfo.pathPatternsCondition?.patterns?.map { it.patternString } ?: emptyList()

            if (httpMethods.isEmpty()) {
                // NOTE: HTTP 메서드 지정이 없으면 모든 메서드에 대해 매처 생성
                patterns.map { AntPathRequestMatcher(it) }
            } else {
                // NOTE: 지정된 HTTP 메서드에 대해서만 매처 생성
                patterns.flatMap { pattern ->
                    httpMethods.map { method -> AntPathRequestMatcher(pattern, method.name) }
                }
            }
        }
        // NOTE: 모든 매처를 OrRequestMatcher로 결합하여 하나라도 일치하면 true가 되도록 함
        this.allEndpointsMatcher = OrRequestMatcher(matchers)
    }

    /**
     * 현재 요청이 정의된 엔드포인트 중 하나와 일치하는지 확인한다.
     */
    fun matches(request: HttpServletRequest): Boolean {
        return allEndpointsMatcher.matches(request)
    }
}