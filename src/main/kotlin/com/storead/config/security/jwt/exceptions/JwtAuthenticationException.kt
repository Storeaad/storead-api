package com.storead.config.security.jwt.exceptions

import com.storead.common.exception.APIException
import org.springframework.http.HttpStatus

class JwtAuthenticationException(
    message: String,
    status: HttpStatus = HttpStatus.UNAUTHORIZED
) : APIException(message, status)