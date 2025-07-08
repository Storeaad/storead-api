package com.storead.common.storage

import org.springframework.web.multipart.MultipartFile

data class UploadFile(
    private val file: MultipartFile
) {

    val originalFilename: String = file.originalFilename ?: ""
    val inputStream = file.inputStream

    fun isGreaterThan(size: Long) = file.size > size

    fun doesNotContains(contentTypes: List<String>) = file.contentType !in contentTypes
}