package com.ulikme.country.api

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@EnableMongoRepositories(basePackages = ["com.ulikme.country.infrastructure.persistence.mongo.repositories"])
@EnableMongoAuditing
@ComponentScan(basePackages = [
    "com.ulikme.country",
    "com.ulikme.utils",
    "dev.payqa.scaffolding.apicrud.design.rest"
])
@SpringBootApplication(scanBasePackages = ["com.ulikme.country"])
class CountryApplication

fun main(args: Array<String>) {
    SpringApplication.run(CountryApplication::class.java, *args)
}