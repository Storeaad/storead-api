package com.storead.article.domain

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ArticleThumbnailImageRepository: JpaRepository<ArticleThumbnailImage, UUID> {
}