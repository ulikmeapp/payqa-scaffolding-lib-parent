package com.ulikme.person.infrastructure.persistence.mongo.repositories

import com.ulikme.person.infrastructure.persistence.mongo.entities.PreferencesEntity
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface PreferencesRepository : MongoRepository<PreferencesEntity, String> {

    fun findByPersonId(personId: String): Optional<PreferencesEntity>

}