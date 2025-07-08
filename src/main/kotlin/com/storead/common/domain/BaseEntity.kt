package com.storead.common.domain

import com.github.f4b6a3.ulid.UlidCreator
import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.*


@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {

    @Id
    @Column(columnDefinition = "UUID")
    val id: UUID = UlidCreator.getMonotonicUlid().toUuid()

    @CreatedDate
    var createdAt: LocalDateTime? = null

    @LastModifiedDate
    var lastModifiedAt: LocalDateTime? = null
}