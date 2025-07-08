package com.storead.article.domain

import java.util.*


data class Tags(
    val tags: List<Tag>
) {

    fun createNewTagsFrom(anotherTags: Tags): Tags {
        val existsTagNames: List<String> = names()

        return Tags(
            anotherTags.asList()
                .filterNot { it.name in existsTagNames }
                .map { Tag(it.name) }
        )
    }

    fun toArticleTags(articleId: UUID): List<ArticleTag> = tags.map { ArticleTag(articleId = articleId, tagId = it.id) }

    fun extend(anotherTags: Tags) = Tags(tags + anotherTags.asList())

    fun asList() = tags.toList()

    fun names() = tags.map { it.name }

}