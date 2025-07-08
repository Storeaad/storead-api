package com.storead.config.web

import com.storead.article.ArticleArgumentResolver
import com.storead.profile.ProfileArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig(
    private val profileArgumentResolver: ProfileArgumentResolver,
    private val articleArgumentResolver: ArticleArgumentResolver
) : WebMvcConfigurer {

    private val addResolvers = listOf(
        profileArgumentResolver,
        articleArgumentResolver,
    )

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.addAll(
            addResolvers
        )
    }
}