package com.ulikme.interaction.service.internal

import com.ulikme.interaction.domain.models.BlockingModel
import com.ulikme.interaction.infrastructure.persistence.mongo.mappers.BlockingModelMapper
import com.ulikme.interaction.infrastructure.persistence.mongo.repositories.BlockingRepository
import com.ulikme.interaction.service.BlockingService
import dev.payqa.scaffolding.apicrud.design.models.Page
import dev.payqa.scaffolding.apicrud.design.rest.rr.request.PaginateRequest
import org.springframework.stereotype.Service

@Service
open class DefaultBlockingService(
    private val repository: BlockingRepository
) : BlockingService {

    override fun paginateByPerson(personId: String, request: PaginateRequest<BlockingModel>): Page<BlockingModel> =
        repository.findAllByPersonId(
            personId,
            request.forRepository(BlockingModel::class.java)
        ).let { page -> Page.of(page.totalElements, BlockingModelMapper.inverseMap(page.content)) }

    override fun block(blocking: BlockingModel): BlockingModel =
        BlockingModelMapper.inverseMap(repository.save(BlockingModelMapper.map(blocking)))

    override fun unBlock(personId: String, blockedPersonId: String) =
        repository.deleteByPersonIdAndBlockedPersonId(personId, blockedPersonId)

}