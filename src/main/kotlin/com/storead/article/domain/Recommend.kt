package com.storead.article.domain

import com.storead.common.domain.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.util.UUID


@Entity
@Table(name = "recommends")
class Recommend(

    @Column(name = "article_id", unique = true, nullable = false)
    val articleId: UUID,

    var count: Int = 0,

    @Id
    @UuidGenerator
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID")
    val id: UUID? = null

) : BaseEntity() {

    fun add() {
        count += 1
    }

    fun remove() {
        count -= 1
    }
}
