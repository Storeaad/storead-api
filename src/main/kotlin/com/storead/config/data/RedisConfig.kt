package com.storead.config.data

import com.storead.config.properties.RedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory


@Configuration
class RedisConfig(
    private val redisProperties: RedisProperties,
) {

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        val redisStandConfig = RedisStandaloneConfiguration(redisProperties.host, redisProperties.port)
        redisStandConfig.username = redisProperties.username
        redisStandConfig.setPassword(redisProperties.password)

        return LettuceConnectionFactory(redisStandConfig)
    }
}