package com.storead.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.file.image")
data class ImageFileProperties(
    val contentTypes: List<String>,
    val extensions: List<String>
)
