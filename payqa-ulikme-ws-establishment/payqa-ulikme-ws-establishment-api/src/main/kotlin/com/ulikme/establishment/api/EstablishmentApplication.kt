package com.ulikme.establishment.api

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@EnableMongoRepositories(basePackages = ["com.ulikme.establishment.infrastructure.persistence.mongo.repositories"])
@EnableMongoAuditing
@ComponentScan(basePackages = [
    "com.ulikme.establishment",
    "com.ulikme.utils",
    "dev.payqa.scaffolding.apicrud.design.rest"
])
@SpringBootApplication(scanBasePackages = ["com.ulikme.establishment"])
class EstablishmentApplication

fun main(args: Array<String>) {
    SpringApplication.run(EstablishmentApplication::class.java, *args)
}