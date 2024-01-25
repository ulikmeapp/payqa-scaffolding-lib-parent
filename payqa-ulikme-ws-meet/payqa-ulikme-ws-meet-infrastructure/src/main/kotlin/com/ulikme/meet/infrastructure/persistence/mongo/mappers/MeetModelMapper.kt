package com.ulikme.meet.infrastructure.persistence.mongo.mappers

import com.ulikme.establishment.infrastructure.persistence.mongo.mappers.EstablishmentModelMapper
import com.ulikme.meet.domain.models.AssistantModel
import com.ulikme.meet.domain.models.MeetModel
import com.ulikme.meet.infrastructure.persistence.mongo.entities.AssistantEntity
import com.ulikme.meet.infrastructure.persistence.mongo.entities.MeetEntity
import dev.payqa.scaffolding.apicrud.design.models.mapper.ModelMapper

object MeetModelMapper : ModelMapper<MeetModel, MeetEntity>() {

    override fun inverseMap(input: MeetEntity): MeetModel =
        MeetModel(
            id = input.id ?: "",
            chatId = input.chatId,
            date = input.date,
            assistants = AssistantModelMapper.inverseMap(input.assistants),
            establishment = input.establishment?.let { EstablishmentModelMapper.inverseMap(it) },
            group = input.group,
            status = input.status
        )

    override fun map(input: MeetModel): MeetEntity =
        MeetEntity(
            id = input.id.takeIf { it.isNotEmpty() },
            chatId = input.chatId,
            date = input.date,
            assistants = AssistantModelMapper.map(input.assistants),
            establishment = input.establishment?.let { EstablishmentModelMapper.map(it) },
            group = input.group,
            status = input.status
        )

}

object AssistantModelMapper : ModelMapper<AssistantModel, AssistantEntity>() {

    override fun inverseMap(input: AssistantEntity): AssistantModel =
        AssistantModel(
            information = input.information,
            qualification = input.qualification,
            confirmed = input.confirmed
        )

    override fun map(input: AssistantModel): AssistantEntity =
        AssistantEntity(
            information = input.information,
            qualification = input.qualification,
            confirmed = input.confirmed
        )

}