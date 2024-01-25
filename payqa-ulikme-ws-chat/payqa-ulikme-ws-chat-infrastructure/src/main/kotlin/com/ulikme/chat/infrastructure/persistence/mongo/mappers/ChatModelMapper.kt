package com.ulikme.chat.infrastructure.persistence.mongo.mappers

import com.ulikme.chat.domain.models.ChatModel
import com.ulikme.chat.domain.models.EMPTY_MESSAGE
import com.ulikme.chat.infrastructure.persistence.mongo.entities.ChatEntity
import com.ulikme.interaction.infrastructure.persistence.mongo.mappers.InteractionModelMapper
import dev.payqa.scaffolding.apicrud.design.models.mapper.ModelMapper

object ChatModelMapper : ModelMapper<ChatModel, ChatEntity>() {

    override fun inverseMap(input: ChatEntity): ChatModel =
        ChatModel(
            id = input.id ?: "",
            participants = input.participants ?: emptyList(),
            interaction = input.interaction?.let { InteractionModelMapper.inverseMap(it) },
            status = input.status ?: "",
            latestMessage = input.latestMessage?.let { MessageModelMapper.inverseMap(it) } ?: EMPTY_MESSAGE
        )

    override fun map(input: ChatModel): ChatEntity =
        ChatEntity(
            id = input.id.takeIf { it.isNotEmpty() },
            participants = input.participants,
            interaction = input.interaction?.let { InteractionModelMapper.map(it) },
            status = input.status,
            latestMessage = input.latestMessage?.let { MessageModelMapper.map(it) }
        )

}
