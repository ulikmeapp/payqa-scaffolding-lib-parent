package com.ulikme.meet.service.internal

import com.ulikme.meet.domain.enums.MeetStatus
import com.ulikme.meet.domain.models.MeetModel
import com.ulikme.meet.infrastructure.persistence.mongo.mappers.MeetModelMapper
import com.ulikme.meet.infrastructure.persistence.mongo.repositories.MeetRepository
import com.ulikme.meet.service.MeetService
import dev.payqa.scaffolding.apicrud.design.exceptions.http.NotFoundException
import dev.payqa.scaffolding.apicrud.design.models.Page
import dev.payqa.scaffolding.apicrud.design.rest.rr.request.PaginateRequest
import org.springframework.stereotype.Service

@Service
open class DefaultMeetService(
    private val repository: MeetRepository
) : MeetService {

    override fun paginate(request: PaginateRequest<MeetModel>): Page<MeetModel> =
        repository.findAllByAssistantInformationId(
            request.params["assistantInformationId"] as String,
            request.forRepository(MeetModel::class.java)
        ).let { page -> Page.of(page.totalElements, MeetModelMapper.inverseMap(page.content)) }

    override fun findById(id: String): MeetModel =
        repository.findById(id).orElseThrow { throw NotFoundException("Cannot find meet with id: $id") }
            .let { MeetModelMapper.inverseMap(it) }

    override fun findProposedByUserOrNull(assistantId: String, chatId: String): MeetModel? =
        repository.findByAssistantInformationIdAndChatIdAndStatus(assistantId, chatId, MeetStatus.PROPOSED)
            .orElse(null)?.let { MeetModelMapper.inverseMap(it) }

    override fun save(meet: MeetModel): MeetModel =
        MeetModelMapper.inverseMap(
            repository.save(
                MeetModelMapper.map(meet)
            )
        )

}