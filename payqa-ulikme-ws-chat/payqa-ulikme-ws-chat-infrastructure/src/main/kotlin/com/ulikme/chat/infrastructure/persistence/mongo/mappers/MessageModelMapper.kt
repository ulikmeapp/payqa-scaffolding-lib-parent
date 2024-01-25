package com.ulikme.chat.infrastructure.persistence.mongo.mappers

import com.ulikme.chat.domain.models.MeetInMessageModel
import com.ulikme.chat.domain.models.MessageModel
import com.ulikme.chat.infrastructure.persistence.mongo.entities.MeetInMessageEntity
import com.ulikme.chat.infrastructure.persistence.mongo.entities.MessageEntity
import com.ulikme.establishment.infrastructure.persistence.mongo.mappers.EstablishmentModelMapper
import dev.payqa.scaffolding.apicrud.design.models.mapper.ModelMapper

object MessageModelMapper : ModelMapper<MessageModel, MessageEntity>() {

    override fun inverseMap(input: MessageEntity): MessageModel =
        MessageModel(
            id = input.id ?: "",
            chatId = input.chatId,
            participant = input.participant,
            type = input.type,
            content = input.content,
            seen = input.seen,
            meetInMessage = input.meetInMessage?.let { MeetInMessageModelMapper.inverseMap(it) },
            edited = input.edited
        )

    override fun map(input: MessageModel): MessageEntity =
        MessageEntity(
            id = input.id.takeIf { it.isNotEmpty() },
            chatId = input.chatId,
            participant = input.participant,
            type = input.type,
            content = input.content,
            seen = input.seen,
            meetInMessage = input.meetInMessage?.let { MeetInMessageModelMapper.map(it) },
            edited = input.edited
        )

}

object MeetInMessageModelMapper : ModelMapper<MeetInMessageModel, MeetInMessageEntity>() {

    override fun map(input: MeetInMessageModel): MeetInMessageEntity =
        MeetInMessageEntity(
            establishment = input.establishment.let { EstablishmentModelMapper.map(it) },
            date = input.date,
            hour = input.hour,
            proposeId = input.proposeId,
            answerIds = input.answerIds
        )

    override fun inverseMap(input: MeetInMessageEntity): MeetInMessageModel =
        MeetInMessageModel(
            establishment = input.establishment.let { EstablishmentModelMapper.inverseMap(it) },
            date = input.date,
            hour = input.hour,
            proposeId = input.proposeId,
            answerIds = input.answerIds
        )

}