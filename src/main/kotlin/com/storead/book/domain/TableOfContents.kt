package com.storead.book.domain

import com.storead.common.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.util.*


data class RawTableOfContents(val book: Book, val tableOfContents: List<String>) {
    fun toEntities(): List<TableOfContents> {
        return tableOfContents.mapIndexed { idx, title ->
            TableOfContents(
                chapterNumber = idx + 1,
                title = title,
                bookId = book.id
            )
        }
    }
}


@Entity
@Table(name = "table_of_contents")
class TableOfContents(

    val chapterNumber: Int,

    val title: String,

    @Column(name = "book_id")
    val bookId: UUID,

    ) : BaseEntity() {
}