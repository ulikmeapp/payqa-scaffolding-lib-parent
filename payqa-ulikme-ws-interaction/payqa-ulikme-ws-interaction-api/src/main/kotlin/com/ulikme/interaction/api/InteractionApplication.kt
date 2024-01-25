package com.ulikme.interaction.api

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@EnableMongoRepositories(basePackages = ["com.ulikme.interaction.infrastructure.persistence.mongo.repositories"])
@EnableMongoAuditing
@ComponentScan(basePackages = [
    "com.ulikme.interaction",
    "com.ulikme.utils",
    "dev.payqa.scaffolding.apicrud.design.rest"
])
@SpringBootApplication(scanBasePackages = ["com.ulikme.interaction"])
class InteractionApplication

fun main(args: Array<String>) {
    SpringApplication.run(InteractionApplication::class.java, *args)
}