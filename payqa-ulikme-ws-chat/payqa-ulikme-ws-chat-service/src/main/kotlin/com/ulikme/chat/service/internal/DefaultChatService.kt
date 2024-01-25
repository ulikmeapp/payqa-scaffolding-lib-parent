package com.ulikme.chat.service.internal

import com.ulikme.chat.domain.models.ChatModel
import com.ulikme.chat.domain.models.MessageModel
import com.ulikme.chat.infrastructure.persistence.mongo.entities.MessageEntity
import com.ulikme.chat.infrastructure.persistence.mongo.mappers.ChatModelMapper
import com.ulikme.chat.infrastructure.persistence.mongo.mappers.MessageModelMapper
import com.ulikme.chat.infrastructure.persistence.mongo.repositories.ChatRepository
import com.ulikme.chat.infrastructure.persistence.mongo.repositories.MessageRepository
import com.ulikme.chat.service.ChatService
import dev.payqa.scaffolding.apicrud.design.exceptions.http.NotFoundException
import dev.payqa.scaffolding.apicrud.design.models.Page
import dev.payqa.scaffolding.apicrud.design.rest.rr.request.PaginateRequest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
open class DefaultChatService(
    private val repository: ChatRepository,
    private val messageRepository: MessageRepository
) : ChatService {

    override fun paginate(request: PaginateRequest<ChatModel>): Page<ChatModel> =
        repository.findAllByParticipantId(
            request.params["personId"] as String,
            request.forRepository(ChatModel::class.java)
        ).let { page -> Page.of(page.totalElements, ChatModelMapper.inverseMap(page.content)) }

    // TODO: Implement cache only when param <withLatestMessages> be false (programmatically)
    override fun findById(id: String, withLatestMessages: Boolean): ChatModel =
        ChatModelMapper.inverseMap(
            repository.findById(id).orElseThrow { NotFoundException("Cannot found a chat with id: $id") }
        ).copy(
            latestMessages = mutableListOf<MessageModel>().apply {
                if (withLatestMessages)
                    addAll(
                        messageRepository.findAllByChatIdOrderByCreatedDateDesc(
                            id, PageRequest.of(
                                0, 10,
                                Sort.by(Sort.Direction.DESC, MessageEntity::createdDate.name)
                            )
                        ).let { MessageModelMapper.inverseMap(it.content) }
                    )
            }
        )

    override fun save(chat: ChatModel): ChatModel =
        ChatModelMapper.inverseMap(
            repository.save(
                ChatModelMapper.map(chat)
            )
        )

}