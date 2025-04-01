package com.storead.book.signal

import com.storead.book.domain.Book

data class BookCreateEvent(
    val instance: Book
)
