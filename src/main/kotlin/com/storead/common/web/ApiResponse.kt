package com.storead.common.web

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity


data class ApiResponse<T>(
    val data: T,
    val message: String,
    val status: HttpStatus,
) {
    val code: Int = status.value()

    companion object {

        fun <T> success(data: T, status: HttpStatus = HttpStatus.OK): ResponseEntity<ApiResponse<T>> =
            ResponseEntity.status(status).body(ApiResponse(data, status.name, status))

        fun <T> success(
            data: T,
            cookies: List<ResponseCookie>,
            status: HttpStatus = HttpStatus.OK
        ): ResponseEntity<ApiResponse<T>> {
            val response = ApiResponse(data, status.name, status)
            val headers = HttpHeaders()
            cookies.forEach { headers.add(HttpHeaders.SET_COOKIE, it.toString()) }
            return ResponseEntity.status(status).headers(headers).body(response)
        }
    }

    fun <T> fail(
        data: T,
        message: String,
        status: HttpStatus = HttpStatus.BAD_REQUEST,
    ): ResponseEntity<ApiResponse<T>> =
        ResponseEntity.status(status).body(ApiResponse(data, message, status))
}
