package com.storead.reaction.domain

import com.storead.common.domain.BaseEntity
import com.storead.common.domain.EntityType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.util.UUID


@Entity
class Reaction(

    val userId: UUID,

    val subjectId: UUID,

    @Enumerated(EnumType.STRING)
    val subjectType: EntityType,

    @Enumerated(EnumType.STRING)
    var reactionType: ReactionType,

): BaseEntity() {
    fun updateReactionType(reactionType: ReactionType) {
        this.reactionType = reactionType
    }

    fun hasAlreadyReacted(userId: UUID, subjectId: UUID, reactionType: ReactionType): Boolean {
        return this.userId == userId
                && this.subjectId == subjectId
                && this.reactionType == reactionType
    }

}