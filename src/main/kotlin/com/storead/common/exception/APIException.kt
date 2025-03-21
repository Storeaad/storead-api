package com.storead.common.exception

import org.springframework.http.HttpStatus

open class APIException(message: String, val status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR): RuntimeException(message) {
}