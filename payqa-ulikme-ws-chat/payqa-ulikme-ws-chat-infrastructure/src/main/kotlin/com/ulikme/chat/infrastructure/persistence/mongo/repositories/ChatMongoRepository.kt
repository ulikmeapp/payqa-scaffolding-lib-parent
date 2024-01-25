package com.ulikme.chat.infrastructure.persistence.mongo.repositories

import com.ulikme.chat.infrastructure.persistence.mongo.entities.ChatEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ChatMongoRepository {

    fun findAllByParticipantId(participantId: String, pageable: Pageable): Page<ChatEntity>

}