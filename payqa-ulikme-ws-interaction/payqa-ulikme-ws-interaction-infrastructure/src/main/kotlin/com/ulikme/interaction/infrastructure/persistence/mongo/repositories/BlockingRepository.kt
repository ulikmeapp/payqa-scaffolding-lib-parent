package com.ulikme.interaction.infrastructure.persistence.mongo.repositories

import com.ulikme.interaction.infrastructure.persistence.mongo.entities.BlockingEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface BlockingRepository : MongoRepository<BlockingEntity, String> {

    fun findAllByPersonId(personId: String, pageable: Pageable): Page<BlockingEntity>

    fun deleteByPersonIdAndBlockedPersonId(personId: String, blockedPersonId: String)

}