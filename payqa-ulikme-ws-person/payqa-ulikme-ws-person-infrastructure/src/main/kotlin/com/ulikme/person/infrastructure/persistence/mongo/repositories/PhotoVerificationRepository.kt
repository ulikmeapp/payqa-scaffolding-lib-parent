package com.ulikme.person.infrastructure.persistence.mongo.repositories

import com.ulikme.person.infrastructure.persistence.mongo.entities.PhotoVerificationEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface PhotoVerificationRepository : MongoRepository<PhotoVerificationEntity, String> {

    fun findAllByPersonIdOrderByCreatedDateDesc(personId: String, pageable: Pageable): Page<PhotoVerificationEntity>

}