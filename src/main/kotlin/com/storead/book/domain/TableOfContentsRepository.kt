package com.storead.book.domain

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface TableOfContentsRepository : JpaRepository<TableOfContents, UUID> {
}