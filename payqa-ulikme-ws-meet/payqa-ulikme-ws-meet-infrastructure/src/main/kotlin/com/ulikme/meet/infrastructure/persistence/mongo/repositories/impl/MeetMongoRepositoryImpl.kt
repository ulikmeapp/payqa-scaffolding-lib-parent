package com.ulikme.meet.infrastructure.persistence.mongo.repositories.impl

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Indexes
import com.ulikme.establishment.infrastructure.persistence.mongo.entities.EstablishmentEntity
import com.ulikme.meet.domain.enums.MeetStatus
import com.ulikme.meet.infrastructure.persistence.mongo.entities.AssistantEntity
import com.ulikme.meet.infrastructure.persistence.mongo.entities.MeetEntity
import com.ulikme.meet.infrastructure.persistence.mongo.repositories.MeetMongoRepository
import com.ulikme.meet.infrastructure.persistence.mongo.repositories.mapper.MeetMapper
import com.ulikme.person.domain.common.ShortPerson
import com.ulikme.utils.data.utils.GeoUtils
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
open class MeetMongoRepositoryImpl(
    private val mongoTemplate: MongoTemplate
) : MeetMongoRepository {

    override fun findAllByAssistantInformationId(assistantInformationId: String, pageable: Pageable): Page<MeetEntity> =
        Query().apply {
            addCriteria(
                Criteria.where(
                    MeetEntity::assistants.name + "." + AssistantEntity::information.name + "." +
                            ShortPerson::id.name
                ).`in`(assistantInformationId)
            )
            addCriteria(Criteria.where(MeetEntity::status.name).`is`(MeetStatus.CONFIRMED.key))
            with(pageable)
            skip((pageable.pageSize * pageable.pageNumber).toLong())
            limit(pageable.pageSize)
        }.let { query ->
            PageImpl(
                mongoTemplate.find(query, MeetEntity::class.java),
                pageable,
                mongoTemplate.count(query.skip(-1).limit(-1), MeetEntity::class.java)
            )
        }

    override fun findAllByEstablishmentNear(establishmentLatitude: Double, establishmentLongitude: Double): List<MeetEntity> =
        mongoTemplate.getCollection(MeetEntity::class.java.getAnnotation(Document::class.java).collection)
            .let { collection ->
                collection.createIndex(Indexes.geo2dsphere(MeetEntity::establishment.name + "." + EstablishmentEntity::location.name))
                arrayOf(
                    Filters.geoWithinCenterSphere(
                        MeetEntity::establishment.name + "." + EstablishmentEntity::location.name,
                        establishmentLongitude,
                        establishmentLatitude,
                        GeoUtils.calcKm(2.0)
                    )
                ).let { collection.find(Filters.and(*it)) }
            }.map { MeetMapper.map(it) }.toList()

    override fun findByAssistantInformationIdAndChatIdAndStatus(
        assistantInformationId: String,
        chatId: String,
        status: MeetStatus
    ): Optional<MeetEntity> =
        Query().apply {
            addCriteria(
                Criteria.where(
                    MeetEntity::assistants.name + "." + AssistantEntity::information.name + "." +
                            ShortPerson::id.name
                ).`in`(assistantInformationId)
            )
            addCriteria(Criteria.where(MeetEntity::chatId.name).`is`(chatId))
            addCriteria(Criteria.where(MeetEntity::status.name).`is`(status.key))
        }.let { query ->
            Optional.ofNullable(mongoTemplate.find(query, MeetEntity::class.java).firstOrNull())
        }

}