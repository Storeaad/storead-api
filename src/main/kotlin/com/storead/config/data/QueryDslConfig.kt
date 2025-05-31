package com.storead.config.data

import com.querydsl.jpa.JPQLTemplates
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class QueryDslConfig {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    @Bean
    fun jpaQueryFactory(): JPAQueryFactory {

        /**
         * https://github.com/querydsl/querydsl/issues/3428
         *  Hibernate 6.x 버전부터 groupby + transfrom query는 DefaultQueryHandler 를 사용해야함
         */
        return JPAQueryFactory(JPQLTemplates.DEFAULT, entityManager)
    }
}