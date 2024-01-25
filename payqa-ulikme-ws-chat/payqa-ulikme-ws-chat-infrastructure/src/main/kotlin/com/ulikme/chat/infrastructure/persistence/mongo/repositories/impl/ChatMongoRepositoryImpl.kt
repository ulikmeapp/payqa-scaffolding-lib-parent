package com.ulikme.chat.infrastructure.persistence.mongo.repositories.impl

import com.ulikme.chat.infrastructure.persistence.mongo.entities.ChatEntity
import com.ulikme.chat.infrastructure.persistence.mongo.repositories.ChatMongoRepository
import com.ulikme.interaction.infrastructure.persistence.mongo.entities.BlockingEntity
import com.ulikme.person.infrastructure.persistence.mongo.entities.FullPersonEntity
import com.ulikme.utils.data.config.FIELD_ID
import org.bson.types.ObjectId
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.AggregationOperation
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext
import org.springframework.data.mongodb.core.aggregation.TypedAggregation
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.stereotype.Repository
import java.util.*

@Repository
open class ChatMongoRepositoryImpl(
    private val mongoTemplate: MongoTemplate
) : ChatMongoRepository {

    override fun findAllByParticipantId(participantId: String, pageable: Pageable): Page<ChatEntity> {
        val blockingNativeQuery =
            "{ " +
                "\$lookup: { " +
                    "from: '${BlockingEntity::class.java.getAnnotation(Document::class.java).collection}'," +
                    "let: { fullPersonId: '\$${ChatEntity::participants.name}.$FIELD_ID' }," +
                    "pipeline: [{" +
                        "\$match: {" +
                            "\$expr: {" +
                                "\$or: [{" +
                                    "\$and: [{" +
                                        "\$eq: [" +
                                            "'\$${BlockingEntity::createdUser.name}.$FIELD_ID', " +
                                            "${participantId.takeIf { ObjectId.isValid(it) }?.let { "new ObjectId('$it')" } ?: "'$participantId'"} " +
                                        "]" +
                                    "}, {" +
                                        "\$in: [ '\$${BlockingEntity::blockedPerson.name}.$FIELD_ID', '\$\$fullPersonId' ]" +
                                    "}]" +
                                "}, {" +
                                    "\$and: [{" +
                                        "\$in: [ '\$${BlockingEntity::createdUser.name}.$FIELD_ID', '\$\$fullPersonId' ]" +
                                    "}, {" +
                                        "\$eq: [ " +
                                            "'\$${BlockingEntity::blockedPerson.name}.$FIELD_ID', " +
                                            "${participantId.takeIf { ObjectId.isValid(it) }?.let { "new ObjectId('$it')" } ?: "'$participantId'"} " +
                                        "]" +
                                    "}]\n" +
                                "}]" +
                            "}" +
                        "}" +
                    "}]," +
                    "as: '${FullPersonEntity::locks.name}'" +
                "}" +
            "}"

        return listOfNotNull(
            /**
             * Filter only chats where participant is participating
             */
            Aggregation.match(
                Criteria.where(ChatEntity::participants.name + "." + FullPersonEntity::id.name).`in`(participantId)
            ),
            /**
             * Add locks lookup into collection to filter it
             */
            object : AggregationOperation {
                override fun toDocument(context: AggregationOperationContext): org.bson.Document =
                    context.getMappedObject(org.bson.Document.parse(blockingNativeQuery))
            },
            /**
             * Include only persons who don't have a blocking by usr
             */
            Aggregation.match(Criteria.where(FullPersonEntity::locks.name).`is`(Collections.EMPTY_LIST)),
        ).toMutableList().apply {
            add(Aggregation.skip((pageable.pageNumber * pageable.pageSize).toLong()))
            add(Aggregation.limit(pageable.pageSize.toLong()))
        }.let { aggregations ->
            mongoTemplate.aggregate(
                Aggregation.newAggregation(
                    ChatEntity::class.java,
                    *aggregations.toTypedArray()
                ) as TypedAggregation<ChatEntity>,
                ChatEntity::class.java
            ).mappedResults
        }.let { PageImpl(it) }
    }
}