package com.storead.book.application.response

import com.storead.book.domain.Book
import com.storead.book.web.response.BookResponse
import java.time.LocalDate
import java.util.UUID

data class BookServiceResponse(
    private val book: Book,
) {
    val bookUuid: UUID = book.id!!
    val isbn: String = book.isbn
    val title: String = book.title
    val author: String = book.author
    val description: String = book.description
    val publishedDate: LocalDate = book.publishDate
    val thumbnailImageUrl: String? = book.image

    fun toResponse(): BookResponse = BookResponse(this)
}