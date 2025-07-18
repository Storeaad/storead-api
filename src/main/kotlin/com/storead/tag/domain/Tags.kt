package com.storead.tag.domain

import com.storead.article.domain.ArticleTag
import java.util.*


data class Tags(
    private val values: List<Tag>
) {
    companion object {
        fun empty(): Tags = Tags(emptyList())
    }

    fun subtract(existingTags: Tags): Tags {
        val existingTagNames: Set<String> = existingTags.names().toSet()
        val newTags: List<Tag> = values.filterNot { existingTagNames.contains(it.name) }
        return Tags(newTags)
    }

    fun toArticleTags(articleId: UUID): List<ArticleTag> = values.map { ArticleTag(articleId = articleId, tagId = it.id) }

    fun extend(anotherTags: Tags) = Tags(values + anotherTags.values)

    fun asList() = values.toList()

    fun names() = values.map { it.name }

}