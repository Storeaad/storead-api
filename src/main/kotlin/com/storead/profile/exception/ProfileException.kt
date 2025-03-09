package com.storead.profile.exception

import com.storead.common.exception.APIException
import org.springframework.http.HttpStatus

class ProfileException(
    override val message: String,
    status: HttpStatus = HttpStatus.BAD_REQUEST,
): APIException(message)