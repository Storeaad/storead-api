package com.storead.profile.domain

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class FollowPagingRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : FollowPagingRepository {

    override fun findFollowingByFromId(
        profileId: Long,
        limit: Int,
        cursor: Long?
    ): List<Follow> {
        val follow = QFollow.follow

        var condition = follow.from.id.eq(profileId)

        if (cursor != null) {
            condition = condition.and(follow.id.lt(cursor))
        }

        return queryFactory.selectFrom(follow)
            .join(follow.to).fetchJoin()
            .where(condition)
            .orderBy(follow.id.desc())
            .limit(limit.toLong())
            .fetch()
    }

    override fun findFollowersByToId(profileId: Long, limit: Int, cursor: Long?): List<Follow> {
        val follow = QFollow.follow

        var condition = follow.to.id.eq(profileId)

        if (cursor != null) {
            condition = condition.and(follow.id.lt(cursor))
        }

        return queryFactory.selectFrom(follow)
            .join(follow.from).fetchJoin()
            .where(condition)
            .orderBy(follow.id.desc())
            .limit(limit.toLong())
            .fetch()
    }

}