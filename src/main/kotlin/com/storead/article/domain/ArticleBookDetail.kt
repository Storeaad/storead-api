package com.storead.article.domain

import java.time.LocalDate
import java.util.UUID

data class ArticleBookDetail(
    val id: UUID?,
    val title: String?,
    val author: String?,
    val description: String?,
    val image: String?,
    val isbn: String?,
    val publishDate: LocalDate?,
)