package com.ulikme.chat.api.facade.internal

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import com.ulikme.chat.api.facade.MessageFacade
import com.ulikme.chat.domain.enums.MessageType
import com.ulikme.chat.domain.models.ChatModel
import com.ulikme.chat.domain.models.MessageModel
import com.ulikme.chat.domain.rr.request.MessageRequest
import com.ulikme.chat.domain.rr.request.assembler.MessageRequestAssembler
import com.ulikme.chat.infrastructure.communication.rest.client.EstablishmentRetrofitClient
import com.ulikme.chat.infrastructure.communication.rest.client.GroupMeetRetrofitClient
import com.ulikme.chat.infrastructure.communication.rest.client.MeetRetrofitClient
import com.ulikme.chat.infrastructure.communication.rest.client.NotificationRetrofitClient
import com.ulikme.chat.service.ChatService
import com.ulikme.chat.service.MessageService
import com.ulikme.meet.domain.rr.request.MeetRequest
import com.ulikme.person.domain.enums.NotificationType
import com.ulikme.person.domain.rr.request.NotificationRequest
import com.ulikme.utils.http.support.SecurityContext
import com.ulikme.utils.rx.RxBgObserver
import org.springframework.stereotype.Service
import rx.Observable
import rx.schedulers.Schedulers
import java.util.*

@Service
open class DefaultMessageFacade(
    private val messageService: MessageService,
    private val chatService: ChatService,
    private val establishmentRetrofitClient: EstablishmentRetrofitClient,
    private val notificationRetrofitClient: NotificationRetrofitClient,
    private val meetRetrofitClient: MeetRetrofitClient,
    private val groupMeetRetrofitClient: GroupMeetRetrofitClient
) : MessageFacade {

    override fun register(id: String, request: MessageRequest): MessageModel =
        chatService.findById(id).let { chat ->
            messageService.register(
                MessageRequestAssembler.toModel(request).let { messageFromRequest ->
                    messageFromRequest.copy(
                        chatId = chat.id,
                        participant = SecurityContext.getUser().id?.let { sessionId ->
                            chat.participants.first { it.id == sessionId }
                        } ?: chat.participants.first(),
                        meetInMessage = messageFromRequest.meetInMessage?.copy(
                            establishment = establishmentRetrofitClient.findById(request.meet!!.establishmentId!!)
                        )
                    )
                }
            ).also { message ->
                Schedulers.newThread().let { newThread ->
                    Observable.merge(
                        Observable.fromCallable { updateLatestMessageInChat(chat, message) },
                        Observable.fromCallable { answerProposeMessage(chat, message) },
                        Observable.fromCallable { resolveMeetAcceptation(chat, message) },
                        Observable.fromCallable { sendNotificationToPeopleInChat(chat, message) },
                    )
                        .subscribeOn(newThread)
                        .observeOn(newThread)
                        .subscribe(RxBgObserver("backgroundMessageTasks"))
                }
            }
        }

    private fun updateLatestMessageInChat(chat: ChatModel, message: MessageModel) =
        chatService.save(chat.copy(latestMessage = message))

    private fun sendNotificationToPeopleInChat(chat: ChatModel, message: MessageModel) =
        chat.participants.filter { it.id != message.participant.id }.forEach {
            notificationRetrofitClient.send(
                it.id,
                NotificationRequest(
                    content = message.content,
                    type = NotificationType.MESSAGE.name,
                    data = message
                )
            )
        }

    private fun answerProposeMessage(chat: ChatModel, message: MessageModel) =
        message.meetInMessage?.proposeId?.let { proposeId ->
            if (chat.group) messageService.answerPropose(chat.id, message.participant.id, proposeId)
            else messageService.answerPropose(chat.id, message.id, proposeId)
        }

    private fun resolveMeetAcceptation(chat: ChatModel, message: MessageModel) =
        message.type?.let { messageType ->
            when (messageType) {
                MessageType.MEET_ACCEPTED.key ->
                    meetRetrofitClient.register(
                        MeetRequest(
                            chatId = chat.id,
                            date = with(message.meetInMessage!!) { "${date}T$hour:00Z" },
                            assistants = chat.participants.map { it.id },
                            establishmentId = message.meetInMessage!!.establishment.id
                        )
                    )
                MessageType.GROUP_MEET_ACCEPTED.key -> groupMeetRetrofitClient.confirm(chat.id)
                else -> Unit
            }
        }

}