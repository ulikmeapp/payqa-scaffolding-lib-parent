package com.ulikme.establishment.infrastructure.persistence.mongo.repositories

import com.ulikme.establishment.infrastructure.persistence.mongo.entities.EstablishmentEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface EstablishmentMongoRepository {

    fun findAllNearByLatitudeAndLongitude(latitude: Double, longitude: Double, pageable: Pageable): Page<EstablishmentEntity>

}