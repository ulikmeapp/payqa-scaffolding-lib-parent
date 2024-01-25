package com.ulikme.chat.api

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@EnableMongoRepositories(basePackages = ["com.ulikme.chat.infrastructure.persistence.mongo.repositories"])
@EnableMongoAuditing
@ComponentScan(basePackages = [
    "com.ulikme.chat",
    "com.ulikme.utils",
    "dev.payqa.scaffolding.apicrud.design.rest"
])
@SpringBootApplication(scanBasePackages = ["com.ulikme.chat"])
class ChatApplication

fun main(args: Array<String>) {
    SpringApplication.run(ChatApplication::class.java, *args)
}