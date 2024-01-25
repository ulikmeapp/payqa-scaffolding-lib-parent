package com.ulikme.meet.infrastructure.persistence.mongo.repositories

import com.ulikme.meet.domain.enums.MeetStatus
import com.ulikme.meet.infrastructure.persistence.mongo.entities.MeetEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.Optional

interface MeetMongoRepository {

    fun findAllByAssistantInformationId(assistantInformationId: String, pageable: Pageable): Page<MeetEntity>

    fun findAllByEstablishmentNear(establishmentLatitude: Double, establishmentLongitude: Double): List<MeetEntity>

    fun findByAssistantInformationIdAndChatIdAndStatus(
        assistantInformationId: String,
        chatId: String,
        status: MeetStatus = MeetStatus.CONFIRMED
    ): Optional<MeetEntity>

}