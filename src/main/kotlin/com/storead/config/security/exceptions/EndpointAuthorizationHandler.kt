package com.storead.config.security.exceptions

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ProblemDetail
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class EndpointAuthorizationHandler(
    private val objectMapper: ObjectMapper
) : AuthenticationEntryPoint {

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        // 응답 상태를 404 Not Found로 설정
        response.status = HttpStatus.NOT_FOUND.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"

        // GlobalExceptionHandler와 유사한 형식의 응답 본문 생성
        val problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND,
            "요청하신 리소스를 찾을 수 없습니다."
        ).apply {
            title = "리소스를 찾을 수 없음"
            setProperty("timestamp", LocalDateTime.now().format(dateTimeFormatter))
            setProperty("path", request.requestURI)
        }

        // JSON으로 변환하여 응답 스트림에 쓰기
        response.writer.write(objectMapper.writeValueAsString(problemDetail))

    }
}