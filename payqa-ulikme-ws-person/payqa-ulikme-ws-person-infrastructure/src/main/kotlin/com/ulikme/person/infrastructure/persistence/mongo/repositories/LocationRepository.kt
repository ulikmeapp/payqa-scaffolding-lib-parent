package com.ulikme.person.infrastructure.persistence.mongo.repositories

import com.ulikme.person.infrastructure.persistence.mongo.entities.LocationEntity
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface LocationRepository : MongoRepository<LocationEntity, String> {

    fun findFirstByPersonIdOrderByCreatedDateDesc(personId: String): Optional<LocationEntity>

}