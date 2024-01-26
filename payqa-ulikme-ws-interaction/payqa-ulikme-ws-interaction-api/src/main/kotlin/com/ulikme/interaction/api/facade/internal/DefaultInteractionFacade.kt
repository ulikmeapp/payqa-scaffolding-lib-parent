package com.ulikme.interaction.api.facade.internal

import com.ulikme.chat.domain.rr.request.ChatRequest
import com.ulikme.interaction.api.config.PropertiesConfiguration
import com.ulikme.interaction.api.facade.InteractionFacade
import com.ulikme.interaction.domain.enums.InteractionClassification
import com.ulikme.interaction.domain.models.InteractionModel
import com.ulikme.interaction.domain.rr.request.InteractionRequest
import com.ulikme.interaction.domain.rr.request.assembler.InteractionRequestAssembler
import com.ulikme.interaction.infrastructure.communication.rest.client.ChatRetrofitClient
import com.ulikme.interaction.infrastructure.communication.rest.client.PersonRetrofitClient
import com.ulikme.interaction.service.InteractionService
import com.ulikme.person.domain.rr.request.assembler.SearchRequestAssembler
import com.ulikme.utils.http.support.SecurityContext
import com.ulikme.utils.rx.RxBgObserver
import org.springframework.stereotype.Service
import rx.Observable
import rx.schedulers.Schedulers

@Service
open class DefaultInteractionFacade(
    private val interactionService: InteractionService,
    private val personRetrofitClient: PersonRetrofitClient,
    private val chatRetrofitClient: ChatRetrofitClient,
    private val propertiesConfiguration: PropertiesConfiguration
) : InteractionFacade {

    override fun register(request: InteractionRequest): InteractionModel =
        InteractionRequestAssembler.toModel(request).let { interactionFromRequest ->
            personRetrofitClient.findById(SecurityContext.getUser().id!!).let { createdPerson ->
                interactionFromRequest.classification?.let { classification ->
                    // Check if person who will create the interaction and isn't
                    // subscribed stills having available likes or ulikme interactions today
                    /*if (!createdPerson.subscription.subscribed) {
                        when (classification) {
                            InteractionClassification.LIKE.value -> this.isUserInLimitInteractions(
                                createdPerson.id,
                                classification,
                                propertiesConfiguration.maxDailyLike
                            )
                            InteractionClassification.ULIKME.value -> this.isUserInLimitInteractions(
                                createdPerson.id,
                                classification,
                                propertiesConfiguration.maxDailyUlikme
                            )
                            else -> false
                        }.let { isInLimit ->
                            if (isInLimit) {
                                throw UnauthorizedException(
                                    AuthorizationTypeEnum.FOR_PAYMENT,
                                    "You exceed the limit of interaction for today. " +
                                            "Please wait ${DateTimeUtils.hoursUntilFinishDay()} to make " +
                                            "another interaction or subscribe to Premium."
                                )
                            }
                        }
                    }*/
                    // Check if exists a like interaction to the same person to return it
                    interactionService.findByCoupleAndClassificationOrNull(
                        request.personInteractedId!!,
                        SecurityContext.getUser().id!!,
                        classification
                    )
                } ?: interactionService.register(
                    interactionFromRequest.copy(
                        personInteracted = personRetrofitClient.findById(request.personInteractedId!!)
                    )
                ).also { joinInteractionToGroup(it) }
            }
        }

    private fun isUserInLimitInteractions(createdPersonId: String, classification: String, maxInteractions: Int) =
        interactionService.countByCreatedPerson(
            createdPersonId,
            classification = classification,
        ).toInt() >= maxInteractions

    private fun joinInteractionToGroup(mainInteraction: InteractionModel) =
        Schedulers.newThread().let { newThread ->
            Observable.fromCallable {
                personRetrofitClient.findByIdFull(mainInteraction.createdPersonId!!)
                    .let { fullCreatedPerson ->
                        personRetrofitClient.findByIdFull(mainInteraction.personInteracted!!.id)
                            .let { fullPersonInteracted ->
                                interactionService.listMatchesUngroupeds().parallelStream()
                                    .filter { it.id != mainInteraction.id }
                                    .map {
                                        object {
                                            val id: String = it.id
                                            val createdPersonId: String = it.createdPersonId!!
                                            val personInteractedId: String = it.personInteracted!!.id
                                        }
                                    }
                                    .filter { interaction ->
                                        listOf(
                                            interaction.createdPersonId,
                                            interaction.personInteractedId
                                        ).let {
                                            !it.contains(mainInteraction.createdPersonId) &&
                                                    !it.contains(mainInteraction.personInteracted!!.id)
                                        }
                                    }
                                    .filter { interaction ->
                                        personRetrofitClient.listOnly(
                                            SearchRequestAssembler.fromFullPerson(fullCreatedPerson),
                                            interaction.createdPersonId,
                                            interaction.personInteractedId
                                        ).count == 2 && personRetrofitClient.listOnly(
                                            SearchRequestAssembler.fromFullPerson(
                                                fullPersonInteracted
                                            ),
                                            interaction.createdPersonId,
                                            interaction.personInteractedId
                                        ).count == 2
                                    }
                                    .findFirst()
                                    .ifPresent { interactionForGroup ->
                                        chatRetrofitClient.register(
                                            ChatRequest(
                                                participants = listOf(
                                                    mainInteraction.createdPersonId!!,
                                                    mainInteraction.personInteracted!!.id,
                                                    interactionForGroup.createdPersonId,
                                                    interactionForGroup.personInteractedId
                                                )
                                            )
                                        )
                                        interactionService.joinToAGroup(interactionForGroup.id)
                                    }
                            }
                    }
            }
                .subscribeOn(newThread)
                .observeOn(newThread)
                .subscribe(RxBgObserver("createGroupChat"))
        }

}