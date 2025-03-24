package com.storead.book.domain

import com.storead.common.domain.BaseEntity
import jakarta.persistence.*
import java.time.LocalDate


@Entity
@Table(name = "books")
class Book(
    val isbn: String,

    val title: String,

    val author: String,

    val description: String,

    val publishDate: LocalDate,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val image: BookThumbnailImage? = null,

    ): BaseEntity() {
}