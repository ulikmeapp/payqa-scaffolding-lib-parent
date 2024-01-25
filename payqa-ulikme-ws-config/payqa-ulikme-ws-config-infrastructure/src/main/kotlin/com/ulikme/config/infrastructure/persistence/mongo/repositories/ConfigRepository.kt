package com.ulikme.config.infrastructure.persistence.mongo.repositories

import com.ulikme.config.infrastructure.persistence.mongo.entities.ConfigEntity
import org.springframework.data.mongodb.repository.MongoRepository

interface ConfigRepository : MongoRepository<ConfigEntity, String>