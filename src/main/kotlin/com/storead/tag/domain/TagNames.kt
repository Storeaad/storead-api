package com.storead.tag.domain

data class TagNames(
    val tags: List<String>
) {
    companion object {

        fun from(stringTagNames: List<String>?): TagNames {
            if (stringTagNames.isNullOrEmpty()) {
                return TagNames(emptyList())
            }

            return TagNames(stringTagNames)
        }
    }


    fun toTags(): Tags {
        val normalized: List<String> = normalize()

        return Tags(normalized.map { Tag(it) })
    }

    private fun normalize(): List<String> = tags
        .map { it.trim().lowercase() }
        .filter { it.isNotBlank() }
        .distinct()
}