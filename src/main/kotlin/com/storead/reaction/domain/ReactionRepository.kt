package com.storead.reaction.domain

import com.storead.common.domain.EntityType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ReactionRepository : JpaRepository<Reaction, UUID> {
    fun findAllByUserId(userId: UUID): List<Reaction>

    fun findAllBySubjectIdAndSubjectType(subjectId: UUID, subjectType: EntityType): List<Reaction>

    fun deleteAllBySubjectIdAndSubjectType(subjectId: UUID, subjectType: EntityType)
}