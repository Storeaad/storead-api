package com.storead

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StoreadApplication

fun main(args: Array<String>) {
    runApplication<StoreadApplication>(*args)
}
