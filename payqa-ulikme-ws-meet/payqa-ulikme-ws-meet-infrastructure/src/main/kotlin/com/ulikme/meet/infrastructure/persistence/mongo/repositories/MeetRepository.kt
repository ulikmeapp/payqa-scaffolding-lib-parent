package com.ulikme.meet.infrastructure.persistence.mongo.repositories

import com.ulikme.meet.infrastructure.persistence.mongo.entities.MeetEntity
import org.springframework.data.mongodb.repository.MongoRepository

interface MeetRepository : MongoRepository<MeetEntity, String>, MeetMongoRepository {
}