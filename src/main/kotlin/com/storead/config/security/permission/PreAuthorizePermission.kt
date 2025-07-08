package com.storead.config.security.permission

import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.AnnotatedElementUtils
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import kotlin.collections.iterator

@Component
class PreAuthorizePermission(
    private val applicationContext: ApplicationContext
) {
    fun getAllowAnyEndpoints(): Map<String, List<RequestMethod>> {
        val permitAllEndpoints = mutableMapOf<String, MutableList<RequestMethod>>()
        val requestMappingHandlerMapping = applicationContext.getBean(
            "requestMappingHandlerMapping",
            RequestMappingHandlerMapping::class.java
        )
//        val requestMappingHandlerMapping = applicationContext.getBean(RequestMappingHandlerMapping::class.java)

        // UUID-like 경로 필터링용 정규식
        val uuidPathRegex = "\\{.*id.*}".toRegex(RegexOption.IGNORE_CASE)
        val uuidConstraint = "[0-9a-fA-F\\-]{36}"  // UUID 형식 제약 조건

        for ((mappingInfo, handlerMethod) in requestMappingHandlerMapping.handlerMethods) {
            val preAuthorize =
                AnnotatedElementUtils.findMergedAnnotation(handlerMethod.method, PreAuthorize::class.java)
            if (preAuthorize != null && preAuthorize.value == "permitAll()") {
                mappingInfo.pathPatternsCondition?.patterns?.forEach { pattern ->
                    var path = pattern.patternString

                    // UUID-like path 변수명을 가진 경우 → 강제로 정규식 추가
                    if (uuidPathRegex.containsMatchIn(path)) {
                        path = path.replace(Regex("\\{(.*id.*?)\\}"), "{$1:$uuidConstraint}")
                    }

                    val methods = mappingInfo.methodsCondition.methods.ifEmpty {
                        // 메서드가 지정되지 않았다면 모든 HTTP 메서드를 의미
                        RequestMethod.entries
                    }
                    permitAllEndpoints.computeIfAbsent(path) { mutableListOf() }.addAll(methods)
                }
            }
        }
        return permitAllEndpoints
    }
}