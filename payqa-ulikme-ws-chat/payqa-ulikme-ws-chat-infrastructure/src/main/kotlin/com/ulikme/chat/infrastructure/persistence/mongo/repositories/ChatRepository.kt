package com.ulikme.chat.infrastructure.persistence.mongo.repositories

import com.ulikme.chat.infrastructure.persistence.mongo.entities.ChatEntity
import org.springframework.data.mongodb.repository.MongoRepository

interface ChatRepository : MongoRepository<ChatEntity, String>, ChatMongoRepository {

    fun findAllByInteractionIsNull(): List<ChatEntity>

}