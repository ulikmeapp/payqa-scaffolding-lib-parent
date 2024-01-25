package com.ulikme.person.infrastructure.persistence.mongo.repositories

import com.ulikme.person.infrastructure.persistence.mongo.entities.PhotoEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface PhotoRepository : MongoRepository<PhotoEntity, String> {

    fun findAllByPersonIdOrderByCreatedDateDesc(
        personId: String,
        pageable:Pageable
    ): Page<PhotoEntity>

    fun findByPersonIdAndMain(personId: String, isMain: Boolean = true): Optional<PhotoEntity>

    fun findByPersonIdAndFilename(personId: String, filename: String): Optional<PhotoEntity>

    fun deleteByPersonIdAndFilename(personId: String, filename: String)

}