package com.ulikme.meet.service

import com.ulikme.meet.domain.models.MeetModel
import dev.payqa.scaffolding.apicrud.design.models.Page
import dev.payqa.scaffolding.apicrud.design.rest.rr.request.PaginateRequest

interface MeetService {

    fun paginate(request: PaginateRequest<MeetModel>): Page<MeetModel>

    fun findById(id: String): MeetModel

    fun findProposedByUserOrNull(assistantId: String, chatId: String): MeetModel?

    fun save(meet: MeetModel): MeetModel

}