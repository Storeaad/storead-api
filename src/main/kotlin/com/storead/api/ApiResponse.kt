package com.storead.api

import org.springframework.http.HttpStatus


data class ApiResponse<T>(
    val data: T,
    val message: String,
    val status: HttpStatus,
) {
    val code: Int = status.value()

    companion object {
        fun <T> success(data: T, status: HttpStatus = HttpStatus.OK): ApiResponse<T> = ApiResponse(data, status.name, status)

        fun <T> fail(data: T, message: String, status: HttpStatus = HttpStatus.BAD_REQUEST,): ApiResponse<T> = ApiResponse(data, message, status)
    }
}
