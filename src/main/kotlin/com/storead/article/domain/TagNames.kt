package com.storead.article.domain

data class TagNames(
    private val tags: List<String>
) {

    fun toTags(): Tags {
        val normalized: List<String> = normalize()

        return Tags(normalized.map { Tag(it) })
    }

    private fun normalize(): List<String> = tags
        .map { it.trim().lowercase() }
        .filter { it.isNotBlank() }
        .distinct()
}