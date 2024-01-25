package com.ulikme.chat.domain.rr.request.assembler

import com.ulikme.chat.domain.enums.MessageType
import com.ulikme.chat.domain.models.MeetInMessageModel
import com.ulikme.chat.domain.models.MessageModel
import com.ulikme.chat.domain.rr.request.MeetInMessageRequest
import com.ulikme.chat.domain.rr.request.MessageRequest
import com.ulikme.utils.common.DateTimeUtils
import dev.payqa.scaffolding.apicrud.data.exceptions.ValidationException
import dev.payqa.scaffolding.apicrud.data.utils.Validator

object MessageRequestAssembler {

    fun toModel(request: MessageRequest): MessageModel {
        Validator.checkNotNull(request.content, MessageRequest::content.name)
        Validator.checkNotEmpty(request.content, MessageRequest::content.name)
        request.type?.let { type ->
            Validator.checkTrue(
                MessageType.values().any { typeEnum -> typeEnum.key == type },
                "Invalid [${MessageRequest::type.name}]. " +
                        "It must be between the following values: ${MessageType.values()}",
                "invalidType"
            )
        }
        request.meet?.let { meetInMessage ->
            Validator.checkNotNull(meetInMessage.establishmentId, MeetInMessageRequest::establishmentId.name)
            Validator.checkNotEmpty(meetInMessage.establishmentId, MeetInMessageRequest::establishmentId.name)
            Validator.checkNotNull(meetInMessage.date, MeetInMessageRequest::date.name)
            Validator.checkNotEmpty(meetInMessage.date, MeetInMessageRequest::date.name)
            DateTimeUtils.parse(meetInMessage.date!!) ?: throw ValidationException("Invalid entered date")
            Validator.checkNotNull(meetInMessage.hour, MeetInMessageRequest::hour.name)
            Validator.checkNotEmpty(meetInMessage.hour, MeetInMessageRequest::hour.name)
        }
        return MessageModel(
            content = request.content!!,
            type = request.type,
            meetInMessage = request.meet?.let { meetInMessage ->
                MeetInMessageModel(
                    date = meetInMessage.date!!,
                    hour = meetInMessage.hour!!,
                    proposeId = meetInMessage.proposeId
                )
            }
        )
    }

}