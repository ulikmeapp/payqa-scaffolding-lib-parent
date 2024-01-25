package com.ulikme.config.api

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@EnableMongoRepositories(basePackages = ["com.ulikme.config.infrastructure.persistence.mongo.repositories"])
@EnableMongoAuditing
@ComponentScan(basePackages = [
    "com.ulikme.config",
    "com.ulikme.utils",
    "dev.payqa.scaffolding.apicrud.design.rest"
])
@SpringBootApplication(scanBasePackages = ["com.ulikme.config"])
class ConfigApplication

fun main(args: Array<String>) {
    SpringApplication.run(ConfigApplication::class.java, *args)
}