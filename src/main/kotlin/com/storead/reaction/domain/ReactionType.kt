package com.storead.reaction.domain

enum class ReactionType {
    LIKE,  // 👍
    CLAP,  // 👏
    LOVE,  // ❤️
    WOW; // 😮

    companion object {
        fun from(type: String): ReactionType? {
            return entries.find { it.name.equals(type.trim(), ignoreCase = true) }
        }
    }
}