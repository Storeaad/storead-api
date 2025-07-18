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
    val reactionType: ReactionType,

): BaseEntity() {

}