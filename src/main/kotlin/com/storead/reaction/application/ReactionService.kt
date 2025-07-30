package com.storead.reaction.application

import com.storead.reaction.application.request.ReactionServiceCreateRequest
import com.storead.reaction.domain.Reaction
import com.storead.reaction.domain.ReactionRepository
import org.springframework.stereotype.Service


@Service
class ReactionService(
    private val reactionRepository: ReactionRepository,
) {
    fun createReaction(request: ReactionServiceCreateRequest) {
        val existsReaction: Reaction? = reactionRepository.findByUserIdAndSubjectId(
            request.userId,
            request.subjectId,
        )

        if (existsReaction == null) {
            reactionRepository.save(request.toEntity())
            return
        }

        if (existsReaction.hasAlreadyReacted(request.userId, request.subjectId, request.reactionType)) {
            reactionRepository.delete(existsReaction)
            return
        }

        existsReaction.updateReactionType(request.reactionType)
        reactionRepository.save(existsReaction)
        return
    }
}