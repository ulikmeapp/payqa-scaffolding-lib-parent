package com.ulikme.establishment.infrastructure.persistence.mongo.repositories.impl

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Indexes
import com.ulikme.establishment.infrastructure.persistence.mongo.entities.EstablishmentEntity
import com.ulikme.establishment.infrastructure.persistence.mongo.repositories.EstablishmentMongoRepository
import com.ulikme.establishment.infrastructure.persistence.mongo.repositories.mapper.EstablishmentMapper
import com.ulikme.utils.data.utils.GeoUtils
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.geo.Metrics
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.query.NearQuery
import org.springframework.stereotype.Repository
import java.math.BigDecimal

@Repository
open class EstablishmentMongoRepositoryImpl(
    private val mongoTemplate: MongoTemplate
) : EstablishmentMongoRepository {

    override fun findAllNearByLatitudeAndLongitude(
        latitude: Double,
        longitude: Double,
        pageable: Pageable
    ): Page<EstablishmentEntity> =
        PageImpl(
            mongoTemplate
                .getCollection(EstablishmentEntity::class.java.getAnnotation(Document::class.java).collection)
                .let { collection ->
                    collection.createIndex(Indexes.geo2dsphere(EstablishmentEntity::location.name))
                    // Add filters for search
                    arrayOf(
                        Filters.geoWithinCenterSphere(
                            EstablishmentEntity::location.name,
                            longitude,
                            latitude,
                            GeoUtils.calcKm(10.0)
                        )
                    ).let {
                        collection.find(Filters.and(*it))
                            .skip(pageable.pageNumber * pageable.pageSize)
                            .limit(pageable.pageSize)
                    }
                }.map { EstablishmentMapper.map(it) }.toList()
        )

}