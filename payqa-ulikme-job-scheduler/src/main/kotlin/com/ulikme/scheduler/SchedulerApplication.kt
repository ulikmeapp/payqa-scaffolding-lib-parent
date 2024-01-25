package com.ulikme.scheduler

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.scheduling.annotation.EnableScheduling

@EnableMongoRepositories(
    basePackages = [
        "com.ulikme.person.infrastructure.persistence.mongo.repositories",
        "com.ulikme.interaction.infrastructure.persistence.mongo.repositories",
        "com.ulikme.chat.infrastructure.persistence.mongo.repositories",
        "com.ulikme.meet.infrastructure.persistence.mongo.repositories"
    ]
)
@EnableMongoAuditing
@EnableScheduling
@SpringBootApplication
class SchedulerApplication

fun main(args: Array<String>) {
    runApplication<SchedulerApplication>(*args)
}