package com.ulikme.country.infrastructure.persistence.mongo.repositories

import com.ulikme.country.infrastructure.persistence.mongo.entities.CountryEntity
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface CountryRepository : MongoRepository<CountryEntity, String> {

    fun findByCode(code: String): Optional<CountryEntity>

    fun existsByCode(code: String): Boolean

    fun deleteByCode(code: String)

}