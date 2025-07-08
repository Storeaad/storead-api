package com.storead.book.domain

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface BookRepository : JpaRepository<Book, UUID> {
    fun findByIsbn(isbn: String): Book?
}