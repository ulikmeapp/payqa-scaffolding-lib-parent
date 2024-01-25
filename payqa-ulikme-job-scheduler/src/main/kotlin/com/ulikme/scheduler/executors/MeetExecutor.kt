package com.ulikme.scheduler.executors

import com.ulikme.chat.domain.enums.MessageType
import com.ulikme.chat.domain.rr.request.MeetInMessageRequest
import com.ulikme.chat.domain.rr.request.MessageRequest
import com.ulikme.chat.infrastructure.persistence.mongo.repositories.ChatRepository
import com.ulikme.meet.domain.rr.request.MeetRequest
import com.ulikme.person.infrastructure.persistence.mongo.mappers.FullPersonModelMapper
import com.ulikme.person.infrastructure.persistence.mongo.repositories.FullPersonRepository
import com.ulikme.scheduler.communication.rest.auth0.request.TokenRequest
import com.ulikme.scheduler.communication.rest.client.Auth0RetrofitClient
import com.ulikme.scheduler.communication.rest.client.EstablishmentRetrofitClient
import com.ulikme.scheduler.communication.rest.client.GroupMeetRetrofitClient
import com.ulikme.scheduler.communication.rest.client.MessageRetrofitClient
import com.ulikme.utils.http.support.BackendContext
import dev.payqa.scaffolding.apicrud.design.rest.rr.request.PaginateRequest
import dev.payqa.scaffolding.utils.common.DateUtils
import dev.payqa.scaffolding.utils.common.enums.DateFormat
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.streams.toList

@Component
open class MeetExecutor(
    private val tokenRequest: TokenRequest,
    private val chatRepository: ChatRepository,
    private val fullPersonRepository: FullPersonRepository,
    private val auth0RetrofitClient: Auth0RetrofitClient,
    private val establishmentRetrofitClient: EstablishmentRetrofitClient,
    private val messageRetrofitClient: MessageRetrofitClient,
    private val groupMeetRetrofitClient: GroupMeetRetrofitClient
) {

    companion object {
        private val log = LoggerFactory.getLogger(MeetExecutor::class.java)
    }

    @Transactional
    @Scheduled(cron = "0 0 7 * * SAT", zone = "America/New_York") // Every saturday at 7 o'clock
    fun executeGroupReadyToMeet() {
        log.info("[executeGroupReadyToMeet] init.")

        auth0RetrofitClient.generateToken(tokenRequest)
            .let { backendToken -> BackendContext.init(backendToken.access_token) }

        chatRepository.findAllByInteractionIsNull().forEach { groupChat ->
            groupChat.participants!!.parallelStream().map { participant ->
                // Retrieve full person information
                fullPersonRepository.findById(participant.id)
                    // Map to model for better handling
                    .map { FullPersonModelMapper.inverseMap(it) }
                    // Take only with the latest location registered
                    .filter { Objects.nonNull(it.latestLocation) }
                    // Resolve as null if it does not exist
                    .orElse(null)
                    ?.let { fullPerson ->
                        // Retrieve all near establishments according to user's latest location
                        establishmentRetrofitClient.paginateByLocation(
                            fullPerson.latestLocation!!.latitude,
                            fullPerson.latestLocation!!.longitude,
                            PaginateRequest(0, 1)
                        )
                    }
            }
                .toList()
                // Combine into a single list
                .flatten()
                // Transform into a unique list
                .distinct()
                // Take the first found establishment
                .firstOrNull()
                ?.let { closestEstablishment ->
                    Calendar.getInstance().let { calendar ->
                        val stringDate = DateUtils.format(calendar.time, DateFormat.ENGLISH_DATE)
                        val stringHour = "19:00"
                        // Register message into group chat as ready to meet message,
                        // so people can accept or deny it
                        messageRetrofitClient.register(
                            chatId = groupChat.id!!,
                            request = MessageRequest(
                                content = "Ready to meet",
                                type = MessageType.READY_TO_MEET.key,
                                meet = MeetInMessageRequest(
                                    establishmentId = closestEstablishment.id,
                                    date = stringDate,
                                    hour = stringHour
                                )
                            )
                        )
                        // Register a proposed meet with group chat's participants,
                        // therefore as a proposed meet, people won't see it into the application
                        groupMeetRetrofitClient.register(
                            MeetRequest(
                                establishmentId = closestEstablishment.id,
                                assistants = groupChat.participants?.map { it.id },
                                chatId = groupChat.id,
                                date = Calendar.getInstance().let {
                                    "${stringDate}T${stringHour}:00Z"
                                }
                            )
                        )
                    }
                }
        }
    }

}