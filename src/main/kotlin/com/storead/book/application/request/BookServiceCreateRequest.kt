package com.storead.book.application.request

import com.storead.book.domain.Book
import com.storead.book.web.request.BookCreateRequest
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class BookServiceCreateRequest(
    private val bookCreateRequest: BookCreateRequest
) {
    fun toEntity(): Book {
        return Book(
            isbn = bookCreateRequest.isbn,
            title = bookCreateRequest.title,
            author = bookCreateRequest.author,
            description = bookCreateRequest.description,
            publishDate = LocalDate.parse(bookCreateRequest.publishedDate, DateTimeFormatter.ofPattern("yyyyMMdd")),
            image = bookCreateRequest.thumbnailUrl
        )
    }
}