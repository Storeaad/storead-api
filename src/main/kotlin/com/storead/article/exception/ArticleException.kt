package com.storead.article.exception

import com.storead.common.exception.APIException
import com.storead.common.exception.ErrorMessage
import org.springframework.http.HttpStatus

class ArticleException(
    errorMessage: ErrorMessage,
    status: HttpStatus = HttpStatus.BAD_REQUEST,
) : APIException(errorMessage.detail, status)