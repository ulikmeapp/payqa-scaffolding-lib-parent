package com.ulikme.establishment.infrastructure.persistence.mongo.repositories

import com.ulikme.establishment.infrastructure.persistence.mongo.entities.EstablishmentEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface EstablishmentRepository: MongoRepository<EstablishmentEntity, String>, EstablishmentMongoRepository {

    fun findAllByCountryCode(countryCode: String, pageable: Pageable): Page<EstablishmentEntity>

}