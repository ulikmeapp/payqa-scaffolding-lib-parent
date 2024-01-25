package com.ulikme.meet.domain.rr.request.assembler

import com.ulikme.meet.domain.enums.MeetStatus
import com.ulikme.meet.domain.models.MAXIMUM_GROUP_MEET_QUANTITY
import com.ulikme.meet.domain.models.MINIMUM_GROUP_MEET_QUANTITY
import com.ulikme.meet.domain.models.MeetModel
import com.ulikme.meet.domain.models.SIMPLE_MEET_QUANTITY
import com.ulikme.meet.domain.rr.request.MeetRequest
import dev.payqa.scaffolding.apicrud.data.utils.Validator

object MeetRequestAssembler {

    fun toModel(request: MeetRequest): MeetModel {
        Validator.checkNotNull(request.chatId, MeetRequest::chatId.name)
        Validator.checkNotEmpty(request.chatId, MeetRequest::chatId.name)
        Validator.checkNotNull(request.date, MeetRequest::date.name)
        Validator.checkNotEmpty(request.date, MeetRequest::date.name)
        Validator.checkNotNull(request.assistants, MeetRequest::assistants.name)
        Validator.checkTrue(
            request.assistants!!.size == SIMPLE_MEET_QUANTITY,
            "Must there $SIMPLE_MEET_QUANTITY assistants"
        )
        Validator.checkNotNull(request.establishmentId, MeetRequest::establishmentId.name)
        Validator.checkNotEmpty(request.establishmentId, MeetRequest::establishmentId.name)
        return MeetModel(
            chatId = request.chatId!!,
            date = request.date!!
        )
    }

    fun toGroupModel(request: MeetRequest): MeetModel {
        Validator.checkNotNull(request.chatId, MeetRequest::chatId.name)
        Validator.checkNotEmpty(request.chatId, MeetRequest::chatId.name)
        Validator.checkNotNull(request.assistants, MeetRequest::assistants.name)
        Validator.checkTrue(
            request.assistants!!.size >= MINIMUM_GROUP_MEET_QUANTITY,
            "Must there $MINIMUM_GROUP_MEET_QUANTITY at least"
        )
        Validator.checkTrue(
            request.assistants.size <= MAXIMUM_GROUP_MEET_QUANTITY,
            "Must there $MAXIMUM_GROUP_MEET_QUANTITY at most"
        )
        return MeetModel(
            group = true,
            status = MeetStatus.PROPOSED.key
        )
    }

}