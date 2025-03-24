package com.storead.book.domain

import com.storead.common.domain.BaseEntity
import jakarta.persistence.*


@Entity
@Table(name = "book_thumbnails")
class BookThumbnailImage(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val imageUrl: String,

    ): BaseEntity() {
}