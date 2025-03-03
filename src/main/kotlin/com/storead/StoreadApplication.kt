package com.storead

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class StoreadApplication

fun main(args: Array<String>) {
    runApplication<StoreadApplication>(*args)
}
