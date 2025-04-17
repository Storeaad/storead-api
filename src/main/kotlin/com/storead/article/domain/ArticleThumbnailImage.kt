package com.storead.article.domain

import com.storead.common.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table


@Entity
@Table(name = "article_thumbnails")
class ArticleThumbnailImage(

    val thumbnailUrl: String,

    ) : BaseEntity() {


}