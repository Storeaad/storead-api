package com.storead.reaction.domain

enum class ReactionType {
    LIKE,  // 👍
    CLAP,  // 👏
    DISLIKE, // 👎
    LOVE,  // ❤️
    ANGRY,  // 😡
    SAD, // 😢
    WOW; // 😮

    companion object {
        fun from(type: String): ReactionType? {
            return entries.find { it.name.equals(type, ignoreCase = true) }
        }
    }
}