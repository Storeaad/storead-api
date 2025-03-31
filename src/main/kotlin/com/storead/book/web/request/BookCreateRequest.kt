package com.storead.book.web.request

import com.storead.book.application.request.BookServiceCreateRequest

data class BookCreateRequest(
    val isbn: String,
    val title: String,
    val author: String,
    val publishedDate: String,
    val description: String,
    val thumbnailUrl: String,
) {
    fun toServiceRequest(): BookServiceCreateRequest {
        return BookServiceCreateRequest(this)
    }
}
