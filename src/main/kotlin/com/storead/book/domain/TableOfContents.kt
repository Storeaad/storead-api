package com.storead.book.domain

import com.storead.common.domain.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "table_of_contents")
class TableOfContents(

    var displayOrder: Int,

    val chapterNumber: Int,

    val title: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    val book: Book,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val description: String? = null,

    var isModified: Boolean = false,

    ): BaseEntity() {
}