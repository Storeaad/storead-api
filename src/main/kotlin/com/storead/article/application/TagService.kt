package com.storead.article.application

import com.storead.article.domain.ArticleTagRepository
import com.storead.article.domain.TagNames
import com.storead.article.domain.TagRepository
import com.storead.article.domain.Tags
import org.springframework.stereotype.Service
import java.util.*

@Service
class TagService(
    private val tagRepository: TagRepository,
    private val articleTagRepository: ArticleTagRepository,
) {

    fun addAll(tagNames: TagNames): Tags {
        val inputTags: Tags = tagNames.toTags()
        val existsTags: Tags = findExistsTags(inputTags)

        val newTagsFromUserInput: Tags = existsTags.createNewTagsFrom(inputTags)
        tagRepository.saveAll(newTagsFromUserInput.asList())

        return existsTags.extend(newTagsFromUserInput)
    }

    fun tagMappingWithArticle(tags: Tags, articleId: UUID) = articleTagRepository.saveAll(tags.toArticleTags(articleId))

    private fun findExistsTags(tags: Tags): Tags = Tags(tagRepository.findByNameIn(tags.names()))
}