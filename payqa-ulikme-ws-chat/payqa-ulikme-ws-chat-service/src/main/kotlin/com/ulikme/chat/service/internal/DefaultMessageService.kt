package com.ulikme.chat.service.internal

import com.ulikme.chat.domain.models.MessageModel
import com.ulikme.chat.infrastructure.persistence.mongo.mappers.MessageModelMapper
import com.ulikme.chat.infrastructure.persistence.mongo.repositories.MessageRepository
import com.ulikme.chat.service.MessageService
import dev.payqa.scaffolding.apicrud.design.exceptions.http.NotFoundException
import dev.payqa.scaffolding.apicrud.design.models.Page
import dev.payqa.scaffolding.apicrud.design.rest.rr.request.PaginateRequest
import org.springframework.stereotype.Service

@Service
open class DefaultMessageService(
    private val repository: MessageRepository
) : MessageService {

    override fun paginateByChat(chatId: String, request: PaginateRequest<MessageModel>): Page<MessageModel> =
        repository.findAllByChatIdOrderByCreatedDateDesc(
            chatId, request.forRepository(MessageModel::class.java)
        ).let { page -> Page.of(page.totalElements, MessageModelMapper.inverseMap(page.content)) }

    override fun findLatestByChat(chatId: String): MessageModel? =
        repository.findFirstByChatIdOrderByCreatedDate(chatId).orElse(null)
            ?.let { MessageModelMapper.inverseMap(it) }

    override fun findById(id: String): MessageModel =
        MessageModelMapper.inverseMap(repository.findById(id)
            .orElseThrow { throw NotFoundException("Cannot find message with id: $id") })

    override fun findByIdOrNull(id: String): MessageModel? =
        repository.findById(id).orElse(null)?.let { MessageModelMapper.inverseMap(it) }

    override fun findByChatAndId(chatId: String, id: String): MessageModel =
        MessageModelMapper.inverseMap(repository.findByChatIdAndId(chatId, id)
            .orElseThrow {
                throw NotFoundException(
                    "Cannot find message with id: $id, " +
                            "or probably the message does not belong to specified chat"
                )
            })

    override fun register(message: MessageModel): MessageModel =
        MessageModelMapper.inverseMap(
            repository.save(
                MessageModelMapper.map(message)
            )
        )

    override fun update(chatId: String, id: String, content: String): MessageModel =
        MessageModelMapper.inverseMap(
            this.findByChatAndId(chatId, id).let { message ->
                repository.save(
                    MessageModelMapper.map(
                        message.copy(
                            content = content,
                            edited = true
                        )
                    )
                )
            }
        )

    override fun answerPropose(chatId: String, id: String, proposeMessageId: String): MessageModel =
        MessageModelMapper.inverseMap(
            this.findByChatAndId(chatId, proposeMessageId).let { message ->
                repository.save(
                    MessageModelMapper.map(
                        message.copy(
                            meetInMessage = message.meetInMessage?.copy(
                                answerIds = message.meetInMessage!!.answerIds.toMutableList().apply { add(id) }
                            )
                        )
                    )
                )
            }
        )

}