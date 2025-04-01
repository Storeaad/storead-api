package com.storead.book.exception

import com.storead.common.exception.APIException
import org.springframework.http.HttpStatus

class BookException(
    message: String,
    status: HttpStatus = HttpStatus.BAD_REQUEST,
) : APIException(message, status)
