package com.ulikme.meet.infrastructure.persistence.mongo.repositories.mapper

import com.ulikme.establishment.infrastructure.persistence.mongo.repositories.mapper.EstablishmentMapper
import com.ulikme.meet.infrastructure.persistence.mongo.entities.AssistantEntity
import com.ulikme.meet.infrastructure.persistence.mongo.entities.MeetEntity
import com.ulikme.person.domain.common.ShortPerson
import org.bson.Document
import java.util.*

object MeetMapper {

    fun map(meet: Document): MeetEntity =
        MeetEntity(
            id = meet.getObjectId(MeetEntity::id.name).toString(),
            chatId = meet.getString(MeetEntity::chatId.name),
            date = meet.getString(MeetEntity::date.name),
            assistants = mapAssistants(meet.getList(MeetEntity::assistants.name, Document::class.java)),
            establishment = EstablishmentMapper.map(meet.get(MeetEntity::establishment.name, Document::class.java)),
            group = meet.getBoolean(MeetEntity::group.name),
            status = meet.getString(MeetEntity::status.name)
        )

    private fun mapAssistants(assistants: List<Document>?): List<AssistantEntity> =
        assistants?.map { document ->
            AssistantEntity(
                information = Optional.ofNullable(document.get(AssistantEntity::information.name, Document::class.java))
                    .map {
                        ShortPerson(
                            id = it.getObjectId(ShortPerson::id.name).toString(),
                            name = it.getString(ShortPerson::name.name),
                            picture = it.getString(ShortPerson::picture.name),
                            gender = it.getString(ShortPerson::gender.name),
                            age = it.getInteger(ShortPerson::age.name)
                        )
                    }.get(),
                qualification = document.getInteger(AssistantEntity::qualification.name),
                confirmed = document.getBoolean(AssistantEntity::confirmed.name)
            )
        } ?: emptyList()

}