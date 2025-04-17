package com.storead.book.domain

import com.storead.common.domain.BaseEntity
import jakarta.persistence.*


data class RawTableOfContents(val book: Book, val tableOfContents: List<String>) {
    fun toEntities(): List<TableOfContents> {
        return tableOfContents.mapIndexed { idx, title ->
            TableOfContents(
                chapterNumber = idx + 1,
                title = title,
                book = book
            )
        }
    }
}


@Entity
@Table(name = "table_of_contents")
class TableOfContents(

    val chapterNumber: Int,

    val title: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    val book: Book,

    ) : BaseEntity() {
}