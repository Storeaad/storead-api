package com.storead.article.domain

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface TagRepository: JpaRepository<Tag, UUID> {

    fun findByNameIn(names: List<String>): List<Tag>
}