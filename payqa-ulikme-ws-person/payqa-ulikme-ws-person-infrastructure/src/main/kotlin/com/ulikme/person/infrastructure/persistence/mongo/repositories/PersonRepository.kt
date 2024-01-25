package com.ulikme.person.infrastructure.persistence.mongo.repositories

import com.ulikme.person.infrastructure.persistence.mongo.entities.PersonEntity
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface PersonRepository : MongoRepository<PersonEntity, String> {

    fun findByEmail(email: String): Optional<PersonEntity>

    fun deleteByEmail(email: String)

}