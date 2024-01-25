package com.ulikme.person.api

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@EnableMongoRepositories(basePackages = ["com.ulikme.person.infrastructure.persistence.mongo.repositories"])
@EnableMongoAuditing
@ComponentScan(basePackages = [
    "com.ulikme.person",
    "com.ulikme.utils",
    "dev.payqa.scaffolding.apicrud.design.rest",
    "dev.payqa.scaffolding.apicrud.communication.firebase"
])
@SpringBootApplication(scanBasePackages = ["com.ulikme.person"])
class PersonApplication

fun main(args: Array<String>) {
    SpringApplication.run(PersonApplication::class.java, *args)
}