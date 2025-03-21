package com.storead.profile.exception

import com.storead.common.exception.APIException
import org.springframework.http.HttpStatus

class FollowException(
    message: String,
    status: HttpStatus = HttpStatus.BAD_REQUEST,
): APIException(message, status)