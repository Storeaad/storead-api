package com.storead.book.domain

import com.storead.common.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table
import kotlinx.serialization.Serializable
import java.time.LocalDate


@Serializable
data class BookIsbn(val isbn: String)


@Entity
@Table(name = "books")
class Book(
    val isbn: String,

    val title: String,

    val author: String,

    val description: String,

    val publishDate: LocalDate,

    val image: String? = null,

    ) : BaseEntity() {
}