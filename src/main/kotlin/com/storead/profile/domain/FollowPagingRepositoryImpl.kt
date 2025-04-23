package com.storead.profile.domain

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class FollowPagingRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : FollowPagingRepository {

    override fun findFollowingByFromId(
        profileId: UUID,
        limit: Int,
        cursor: UUID?
    ): List<Follow> {
        val follow = QFollow.follow

        val isFollowingProfile = follow.fromId.eq(profileId)

        val condition = cursor?.let {
            isFollowingProfile.and(follow.id.lt(it))
        } ?: isFollowingProfile

        return queryFactory.selectFrom(follow)
            .where(condition)
            .orderBy(follow.id.desc())
            .limit(limit.toLong())
            .fetch()
    }

    override fun findFollowersByToId(profileId: UUID, limit: Int, cursor: UUID?): List<Follow> {
        val follow = QFollow.follow

        val isFollowerProfile = follow.toId.eq(profileId)

        val condition = cursor?.let {
            isFollowerProfile.and(follow.id.lt(it))
        } ?: isFollowerProfile

        return queryFactory.selectFrom(follow)
            .where(condition)
            .orderBy(follow.id.desc())
            .limit(limit.toLong())
            .fetch()
    }

}