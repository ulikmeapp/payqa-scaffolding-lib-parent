package com.ulikme.scheduler.executors

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.ulikme.chat.infrastructure.persistence.mongo.repositories.ChatRepository
import com.ulikme.chat.infrastructure.persistence.mongo.repositories.MessageRepository
import com.ulikme.interaction.infrastructure.persistence.mongo.repositories.InteractionRepository
import com.ulikme.meet.infrastructure.persistence.mongo.repositories.MeetRepository
import com.ulikme.person.domain.models.FullPersonModel
import com.ulikme.person.infrastructure.communication.aws.enums.AwsQueue
import com.ulikme.person.infrastructure.persistence.mongo.mappers.PersonModelMapper
import com.ulikme.utils.rx.RxBgObserver
import dev.payqa.scaffolding.apicrud.communication.aws.client.AwsSqsClient
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import rx.Observable
import rx.schedulers.Schedulers

@Component
open class PersonExecutor(
    private val awsSqsClient: AwsSqsClient,
    private val objectMapper: ObjectMapper,
    private val interactionRepository: InteractionRepository,
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository,
    private val meetRepository: MeetRepository
) {

    companion object {
        private val log = LoggerFactory.getLogger(PersonExecutor::class.java)
    }

    init {
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
    }

    @Transactional
    @Scheduled(cron = "0/15 * * * * *", zone = "America/New_York")
    fun executeUpdateInCollection() {
        log.info("[executeUpdateInCollection] init.")

        awsSqsClient.retrieveMessages(AwsQueue.PERSON_UPDATE.awsName)
            .forEach { awsMessage ->
                val fullPersonMessage = objectMapper.readValue(awsMessage.body(), FullPersonModel::class.java)

                log.info("[executeUpdateInCollection] person message to process: $fullPersonMessage")

                Schedulers.newThread().let { newThread ->
                    Observable.merge(
                        Observable.fromCallable { updatePersonInInteractions(fullPersonMessage) },
                        Observable.fromCallable { updatePersonInChats(fullPersonMessage) },
                        Observable.fromCallable { updatePersonInMessages(fullPersonMessage) },
                        Observable.fromCallable { updatePersonInMeets(fullPersonMessage) }
                    ).subscribeOn(newThread).observeOn(newThread)
                        .subscribe(RxBgObserver("personDetailInformationUpdate"))
                }

                awsSqsClient.deleteMessage(AwsQueue.PERSON_UPDATE.awsName, awsMessage)
            }

        log.info("[executeUpdateInCollection] end.")
    }

    private fun updatePersonInInteractions(fullPerson: FullPersonModel) =
        interactionRepository.findAllByPersonInteractedIdOrderByCreatedDateDesc(fullPerson.id)
            .let { interactionsMadeToPerson ->
                interactionsMadeToPerson.forEach { interaction ->
                    interaction.personInteracted = PersonModelMapper.map(fullPerson.asPerson())
                    interactionRepository.save(interaction)
                }
            }

    private fun updatePersonInChats(fullPerson: FullPersonModel) =
        chatRepository.findAllByParticipantId(fullPerson.id, PageRequest.of(0, Integer.MAX_VALUE)).content
            .let { chatsWherePersonParticipate ->
                chatsWherePersonParticipate.forEach { chat ->
                    chatRepository.save(chat.apply {
                        participants = participants
                            ?.filter { it.id != fullPerson.id }
                            ?.toMutableList()
                            ?.apply { add(fullPerson.asPerson().asShort()) }
                    })
                }
            }

    private fun updatePersonInMessages(fullPerson: FullPersonModel) =
        messageRepository.findAllByParticipantId(fullPerson.id).forEach { message ->
            messageRepository.save(
                message.apply { participant = fullPerson.asPerson().asShort() }
            )
        }

    private fun updatePersonInMeets(fullPerson: FullPersonModel) =
        meetRepository.findAllByAssistantInformationId(fullPerson.id, PageRequest.of(0, Integer.MAX_VALUE)).content
            .let { meetsWillPersonAssist ->
                meetsWillPersonAssist.forEach { meet ->
                    meetRepository.save(meet.apply {
                        val personAssistantInformation = assistants.first { it.information?.id == fullPerson.id }
                        assistants = assistants
                            .filter { it.information?.id != fullPerson.id }
                            .toMutableList()
                            .apply {
                                add(personAssistantInformation.apply {
                                    information = fullPerson.asPerson().asShort()
                                })
                            }
                    })
                }
            }

}