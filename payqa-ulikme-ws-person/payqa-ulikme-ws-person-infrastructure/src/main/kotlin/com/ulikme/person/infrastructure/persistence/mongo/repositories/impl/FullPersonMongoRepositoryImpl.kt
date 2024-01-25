package com.ulikme.person.infrastructure.persistence.mongo.repositories.impl

import com.mongodb.client.model.Indexes
import com.ulikme.interaction.infrastructure.persistence.mongo.entities.BlockingEntity
import com.ulikme.interaction.infrastructure.persistence.mongo.entities.InteractionEntity
import com.ulikme.person.domain.models.ALL_GENDERS
import com.ulikme.person.domain.models.DEFAULT_AGE
import com.ulikme.person.domain.models.DEFAULT_DISTANCE
import com.ulikme.person.domain.models.enums.Gender
import com.ulikme.person.domain.rr.request.SearchRequest
import com.ulikme.person.infrastructure.persistence.mongo.entities.FullPersonEntity
import com.ulikme.person.infrastructure.persistence.mongo.repositories.FullPersonMongoRepository
import com.ulikme.utils.data.config.FIELD_ID
import org.apache.commons.lang3.StringUtils
import org.bson.Document
import org.bson.types.ObjectId
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.geo.Metrics
import org.springframework.data.geo.Point
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.AggregationOperation
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext
import org.springframework.data.mongodb.core.aggregation.TypedAggregation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.NearQuery
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.util.*
import org.springframework.data.mongodb.core.mapping.Document as DocumentAnnotation

@Repository
open class FullPersonMongoRepositoryImpl(
    private val mongoTemplate: MongoTemplate
) : FullPersonMongoRepository {

    override fun findAll(params: Map<String, Any>, pageable: Pageable): Page<FullPersonEntity> =
        buildAggregations(params).toMutableList()
            .apply {
                add(Aggregation.skip((pageable.pageNumber * pageable.pageSize).toLong()))
                add(Aggregation.limit(pageable.pageSize.toLong()))
            }.let { aggregations ->
                mongoTemplate.aggregate(
                    Aggregation.newAggregation(
                        FullPersonEntity::class.java,
                        *aggregations.toTypedArray()
                    ) as TypedAggregation<FullPersonEntity>,
                    FullPersonEntity::class.java
                ).mappedResults
            }.let { PageImpl(it) }

    override fun findOnly(params: Map<String, Any>, vararg includedIds: String): List<FullPersonEntity> =
        params.toMutableMap()
            .apply { put("includedIds", includedIds) }
            .let { newParams ->
                buildAggregations(newParams).let { aggregations ->
                    mongoTemplate.aggregate(
                        Aggregation.newAggregation(
                            FullPersonEntity::class.java,
                            *aggregations.toTypedArray()
                        ) as TypedAggregation<FullPersonEntity>,
                        FullPersonEntity::class.java
                    ).mappedResults
                }
            }

    private fun buildAggregations(params: Map<String, Any>): List<AggregationOperation>  =
        (params[SearchRequest::usr.name] as String).let { personId ->
            /**
             * Query to check that usr won't have results with already interacted persons (match, ulikme, like & nope)
             */
            val interactionNativeQuery =
                "{ " +
                    "\$lookup: { " +
                        "from: '${InteractionEntity::class.java.getAnnotation(DocumentAnnotation::class.java).collection}'," +
                        "let: { personId: '\$$FIELD_ID' }," +
                        "pipeline: [{" +
                            "\$match: {" +
                                "\$expr: {" +
                                    "\$and: [" +
                                        "{ \$eq: [ " +
                                            "'\$${InteractionEntity::createdUser.name}.$FIELD_ID', " +
                                            "${personId.takeIf { ObjectId.isValid(it) }?.let { "new ObjectId('$it')" } ?: "'$personId'"} " +
                                        "]}," +
                                        "{ \$eq: [ '\$${InteractionEntity::personInteracted.name}.$FIELD_ID', '\$\$personId' ]}" +
                                    "]" +
                                "}" +
                            "}" +
                        "}]," +
                        "as: '${FullPersonEntity::interactions.name}'" +
                    "}" +
                "}"

            mongoTemplate.getCollection(FullPersonEntity::class.java.getAnnotation(DocumentAnnotation::class.java).collection)
                .createIndex(Indexes.geo2dsphere(FullPersonEntity::latestLocation.name))

            listOfNotNull(
                /**
                 * Include only persons who stay into geo preferences
                 */
                when (params.containsKey(SearchRequest::ctr.name)) {
                    true -> null
                    false -> Aggregation.geoNear(
                        NearQuery.near(
                            Point(
                                (params[SearchRequest::lng.name] as BigDecimal).toDouble(),
                                (params[SearchRequest::lat.name] as BigDecimal).toDouble(),
                            ),
                            Metrics.MILES
                        ).maxDistance(
                            (params.getOrDefault(SearchRequest::dst.name, DEFAULT_DISTANCE) as Int).toDouble()
                        ).spherical(true),
                        FullPersonEntity::distance.name
                    )
                },
                /**
                 * Exclude usr
                 */
                Aggregation.match(Criteria.where(FIELD_ID).ne(personId)),
                /**
                 * Include only specific ids for accurate search
                 */
                params.getOrDefault("includedIds", null)
                    ?.let { it as Array<*> }
                    ?.let { includedIds -> Aggregation.match(Criteria.where(FIELD_ID).`in`(*includedIds)) },
                /**
                 * Exclude persons who haven't uploaded a photo at least
                 */
                Aggregation.match(Criteria.where(FullPersonEntity::photos.name).ne(Collections.EMPTY_LIST)),
                /**
                 * Add interactions lookup into collection to filter it
                 */
                object : AggregationOperation {
                    override fun toDocument(context: AggregationOperationContext): Document =
                        context.getMappedObject(Document.parse(interactionNativeQuery))
                },
                /**
                 * Exclude persons who are more than preferred age
                 */
                params.getOrDefault(SearchRequest::age.name, DEFAULT_AGE)
                    .let { age -> Aggregation.match(Criteria.where(FullPersonEntity::age.name).lte(age)) },
                /**
                 * Exclude persons who are from gender different than preferred
                 */
                params.getOrDefault(SearchRequest::sm.name, StringUtils.EMPTY)
                    .let { it as String }
                    .let { if (it in StringUtils.EMPTY..ALL_GENDERS) Gender.values().map { gender -> gender.key } else listOf(it) }
                    .let { showMe -> Aggregation.match(Criteria.where(FullPersonEntity::gender.name).`in`(*showMe.toTypedArray())) },
                /**
                 * Include only persons who don't have an interaction with usr
                 */
                Aggregation.match(Criteria.where(FullPersonEntity::interactions.name).`is`(Collections.EMPTY_LIST)),
            )
        }

}