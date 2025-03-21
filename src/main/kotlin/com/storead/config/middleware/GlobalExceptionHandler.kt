package com.storead.config.middleware

import com.storead.common.exception.APIException
import com.storead.common.web.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(Exception::class)
    fun handleCommonException(e: APIException): ResponseEntity<ApiResponse<String>> {
        return ApiResponse.fail(
            data = "",
            message = e.message.toString(),
            status = e.status
        )
    }
}