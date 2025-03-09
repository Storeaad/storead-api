package com.storead.config.middleware

import com.storead.common.web.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.multipart.MaxUploadSizeExceededException


@ControllerAdvice
class FileSizeLimitUploadAdvice {

    @ExceptionHandler(MaxUploadSizeExceededException::class)
    fun handleMaxSizeException(exc: MaxUploadSizeExceededException?): ResponseEntity<ApiResponse<String?>> {
        return ApiResponse.fail(
            null,
            "파일 크기는 최대 10MB 까지 허용됩니다.",
            status = HttpStatus.PRECONDITION_FAILED
        )
    }

}

