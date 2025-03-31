package com.storead.book.domain

import com.storead.common.domain.BaseEntity
import jakarta.persistence.*
import kotlinx.serialization.Serializable
import org.hibernate.annotations.UuidGenerator
import java.time.LocalDate
import java.util.*


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

    @Id
    @UuidGenerator
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID")
    val id: UUID? = null,

    val image: String? = null,

    ) : BaseEntity() {
}