package com.storead.reaction.application.request

import com.storead.common.domain.EntityType
import com.storead.reaction.domain.Reaction
import com.storead.reaction.domain.ReactionType
import java.util.UUID

data class ReactionServiceCreateRequest(
    val userId: UUID,
    val subjectId: UUID,
    val subjectType: EntityType,
    val reactionType: ReactionType
) {
    fun toEntity(): Reaction {
        return Reaction(
            userId,
            subjectId,
            subjectType,
            reactionType
        )
    }
}