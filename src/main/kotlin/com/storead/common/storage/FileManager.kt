package com.storead.common.storage


interface FileManager {

    fun saveImage(file: UploadFile): StoredFile

    fun validate(file: UploadFile): FileManager

}