package com.storead.config.security.jwt.exceptions

import com.storead.common.exception.APIException
import org.springframework.http.HttpStatus

class JwtAuthenticationException(
    override val message: String,
    val status: HttpStatus = HttpStatus.UNAUTHORIZED
) : APIException(message)