package com.storead.tag.application

import com.storead.tag.domain.TagNames
import com.storead.tag.domain.TagRepository
import com.storead.tag.domain.Tags
import org.springframework.stereotype.Service

@Service
class TagService(
    private val tagRepository: TagRepository,
) {

    fun saveTags(tagNames: TagNames?): Tags {
        if (tagNames == null) return Tags.empty()

        val requestTags: Tags = tagNames.toTags()
        val existsTags: Tags = findExistsTags(requestTags)

        val newTags: Tags = requestTags.subtract(existsTags)
        tagRepository.saveAll(newTags.asList())

        return existsTags.extend(newTags)
    }

    private fun findExistsTags(tags: Tags): Tags = Tags(tagRepository.findByNameIn(tags.names()))
}