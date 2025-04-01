package com.storead.book.web.response

import com.storead.book.application.response.BookServiceResponse
import java.time.LocalDate
import java.util.*

data class BookResponse(
    private val bookServiceResponse: BookServiceResponse
) {
    val id: UUID = bookServiceResponse.bookUuid
    val isbn: String = bookServiceResponse.isbn
    val title: String = bookServiceResponse.title
    val author: String = bookServiceResponse.author
    val description: String = bookServiceResponse.description
    val publishedDate: LocalDate = bookServiceResponse.publishedDate
    val thumbnailImageUrl: String? = bookServiceResponse.thumbnailImageUrl
}
