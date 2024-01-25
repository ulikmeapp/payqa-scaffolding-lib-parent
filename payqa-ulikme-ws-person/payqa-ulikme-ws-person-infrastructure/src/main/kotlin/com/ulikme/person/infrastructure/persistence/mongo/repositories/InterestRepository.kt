package com.ulikme.person.infrastructure.persistence.mongo.repositories

import com.ulikme.person.infrastructure.persistence.mongo.entities.InterestEntity
import org.springframework.data.mongodb.repository.MongoRepository

interface InterestRepository : MongoRepository<InterestEntity, String> {

    fun findByShowInOnBoarding(showInOnBoarding: Boolean = true): List<InterestEntity>

}