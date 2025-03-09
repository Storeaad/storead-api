package com.storead.auth.exception

import com.storead.common.exception.APIException
import org.springframework.http.HttpStatus

class AuthException(
    override val message: String,
    val status: HttpStatus = HttpStatus.BAD_REQUEST,
): APIException(message)