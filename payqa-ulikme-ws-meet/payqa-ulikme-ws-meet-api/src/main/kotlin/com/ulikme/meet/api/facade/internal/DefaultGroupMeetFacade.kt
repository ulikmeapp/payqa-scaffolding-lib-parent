package com.ulikme.meet.api.facade.internal

import com.ulikme.meet.api.facade.GroupMeetFacade
import com.ulikme.meet.domain.enums.MeetStatus
import com.ulikme.meet.domain.models.AssistantModel
import com.ulikme.meet.domain.models.MeetModel
import com.ulikme.meet.domain.rr.request.MeetRequest
import com.ulikme.meet.domain.rr.request.assembler.MeetRequestAssembler
import com.ulikme.meet.infrastructure.communication.rest.client.EstablishmentRetrofitClient
import com.ulikme.meet.infrastructure.communication.rest.client.PersonRetrofitClient
import com.ulikme.meet.service.MeetService
import com.ulikme.person.domain.models.BLUR_IMAGE_NAME
import com.ulikme.person.domain.rr.request.assembler.PhotoRequestAssembler
import com.ulikme.utils.http.support.SecurityContext
import dev.payqa.scaffolding.apicrud.design.exceptions.ApiException
import dev.payqa.scaffolding.apicrud.design.exceptions.http.InternalServerErrorException
import dev.payqa.scaffolding.apicrud.design.exceptions.http.NotFoundException
import org.springframework.stereotype.Component
import java.util.*
import javax.servlet.http.HttpServletRequest

@Component
open class DefaultGroupMeetFacade(
    private val meetService: MeetService,
    private val personRetrofitClient: PersonRetrofitClient,
    private val establishmentRetrofitClient: EstablishmentRetrofitClient
) : GroupMeetFacade {

    override fun register(
        request: MeetRequest,
        servletRequest: HttpServletRequest
    ): MeetModel =
        MeetRequestAssembler.toGroupModel(request).let { meetFromRequest ->
            request.assistants!!.let { assistants ->
                assistants.forEach { assistantId ->
                    if (Objects.nonNull(meetService.findProposedByUserOrNull(assistantId, meetFromRequest.chatId!!)))
                        throw ApiException("There is a user inside the assistants that has already a group meet proposed.")
                }
                meetService.save(
                    meetFromRequest.copy(
                        assistants = assistants
                            .map { assistantId -> personRetrofitClient.findById(assistantId) }
                            .map { assistant ->
                                AssistantModel(
                                    information = assistant.let { person ->
                                        person.copy(
                                            complementary = person.complementary.copy(
                                                PhotoRequestAssembler.buildPictureUrl(servletRequest, person.id, BLUR_IMAGE_NAME)
                                            )
                                        ).asShort()
                                    },
                                    confirmed = false
                                )
                            },
                        establishment = establishmentRetrofitClient.findById(request.establishmentId!!)
                    )
                )
            }
        }

    override fun confirm(chatId: String) {
        SecurityContext.getUser().id?.let { userSessionId ->
            meetService.findProposedByUserOrNull(userSessionId, chatId)
                ?.let { proposedMeet ->
                    proposedMeet.assistants
                        .first { it.information!!.id == userSessionId }
                        .let { meetAssistant ->
                            if (!meetAssistant.confirmed) {
                                // Confirm meet only for user in session
                                proposedMeet.assistants.toMutableList()
                                    .replaceAll { iteratorAssistant ->
                                        iteratorAssistant.takeIf { it.information!!.id != userSessionId }
                                            ?: meetAssistant.copy(confirmed = true)
                                    }
                                meetService.save(
                                    proposedMeet.copy(
                                        status = if (proposedMeet.assistants.all { it.confirmed })
                                            MeetStatus.CONFIRMED.key else MeetStatus.PROPOSED.key
                                    )
                                )
                            }
                        }
                } ?: throw NotFoundException("The user has not a proposed group meet.")
        } ?: throw InternalServerErrorException("Invalid user into the current session.")
    }

}