package com.storead.config.middleware

import com.storead.common.exception.APIException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RestControllerAdvice
class GlobalExceptionHandler {

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")


    @ExceptionHandler(APIException::class)
    fun handleAPIException(e: APIException): ProblemDetail {
        val problemDetail = ProblemDetail.forStatusAndDetail(
            e.status,
            e.message ?: "오류가 발생했습니다"
        )
        problemDetail.title = "API 요청 오류"
        problemDetail.setProperty("timestamp", LocalDateTime.now().format(dateTimeFormatter))

        return problemDetail
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(e: Exception): ProblemDetail {
        val problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())

        problemDetail.title = "서버 오류"
        problemDetail.detail = "서버에서 예상치 못한 오류가 발생했습니다"
        problemDetail.setProperty("timestamp", LocalDateTime.now().format(dateTimeFormatter))
        problemDetail.setProperty("exception", e.javaClass.simpleName)

        return problemDetail
    }
}