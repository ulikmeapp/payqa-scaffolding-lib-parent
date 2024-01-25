package com.ulikme.person.infrastructure.persistence.mongo.repositories

import com.ulikme.person.infrastructure.persistence.mongo.entities.NotificationEntity
import org.springframework.data.mongodb.repository.MongoRepository

interface NotificationRepository : MongoRepository<NotificationEntity, String> {

    fun findAllByPersonId(personId: String): List<NotificationEntity>

    fun findAllByPersonIdAndSend(personId: String, send: Boolean = false): List<NotificationEntity>

}