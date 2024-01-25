package com.ulikme.interaction.service

import com.ulikme.interaction.domain.models.BlockingModel
import dev.payqa.scaffolding.apicrud.design.models.Page
import dev.payqa.scaffolding.apicrud.design.rest.rr.request.PaginateRequest

interface BlockingService {

    fun paginateByPerson(personId: String, request: PaginateRequest<BlockingModel>): Page<BlockingModel>

    fun block(blocking: BlockingModel): BlockingModel

    fun unBlock(personId: String, blockedPersonId: String)

}