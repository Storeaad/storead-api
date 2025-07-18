package com.storead.reaction.domain

import com.storead.common.domain.EntityType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ReactionRepository : JpaRepository<Reaction, UUID> {

    /**
     * 사용자가 특정 주체(게시글 등)에 어떤 반응을 했는지 조회합니다.
     */
    fun findByUserIdAndSubjectTypeAndSubjectId(userId: UUID, subjectType: EntityType, subjectId: UUID): Reaction?

    /**
     * 사용자가 반응한 모든 내역을 조회합니다.
     */
    fun findAllByUserId(userId: UUID): List<Reaction>

    /**
     * 특정 주체에 달린 모든 반응을 조회합니다. (반응 개수 집계 등에 사용)
     */
    fun findAllBySubjectIdAndSubjectType(subjectId: UUID, subjectType: EntityType): List<Reaction>

    /**
     * 특정 주체가 삭제될 때, 관련된 모든 반응을 삭제합니다.
     */
    fun deleteAllBySubjectIdAndSubjectType(subjectId: UUID, subjectType: EntityType)

}
