package com.ulikme.person.infrastructure.persistence.mongo.repositories

import com.ulikme.person.infrastructure.persistence.mongo.entities.FullPersonEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface FullPersonRepository : MongoRepository<FullPersonEntity, String>, FullPersonMongoRepository