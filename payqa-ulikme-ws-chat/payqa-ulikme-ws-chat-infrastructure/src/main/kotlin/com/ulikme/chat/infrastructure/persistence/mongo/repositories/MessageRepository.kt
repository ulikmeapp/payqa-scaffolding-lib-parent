package com.ulikme.chat.infrastructure.persistence.mongo.repositories

import com.ulikme.chat.infrastructure.persistence.mongo.entities.MessageEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface MessageRepository : MongoRepository<MessageEntity, String> {

    fun findAllByChatIdOrderByCreatedDateDesc(chatId: String, pageable: Pageable): Page<MessageEntity>

    fun findAllByParticipantId(participantId: String): List<MessageEntity>

    fun findFirstByChatIdOrderByCreatedDate(chatId: String): Optional<MessageEntity>

    fun findByChatIdAndId(chatId: String, id: String): Optional<MessageEntity>

}