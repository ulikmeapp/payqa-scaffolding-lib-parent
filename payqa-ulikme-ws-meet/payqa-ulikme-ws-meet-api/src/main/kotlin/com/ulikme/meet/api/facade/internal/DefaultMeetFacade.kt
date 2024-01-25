package com.ulikme.meet.api.facade.internal

import com.ulikme.meet.api.facade.MeetFacade
import com.ulikme.meet.domain.enums.QualificationType
import com.ulikme.meet.domain.models.AssistantModel
import com.ulikme.meet.domain.models.MeetModel
import com.ulikme.meet.domain.rr.request.MeetRequest
import com.ulikme.meet.domain.rr.request.assembler.MeetRequestAssembler
import com.ulikme.meet.infrastructure.communication.rest.client.EstablishmentRetrofitClient
import com.ulikme.meet.infrastructure.communication.rest.client.PersonRetrofitClient
import com.ulikme.meet.service.MeetService
import com.ulikme.utils.http.support.SecurityContext
import dev.payqa.scaffolding.apicrud.data.utils.Validator
import org.apache.commons.lang3.math.NumberUtils
import org.springframework.stereotype.Service

@Service
open class DefaultMeetFacade(
    private val meetService: MeetService,
    private val personRetrofitClient: PersonRetrofitClient,
    private val establishmentRetrofitClient: EstablishmentRetrofitClient
) : MeetFacade {

    override fun register(request: MeetRequest): MeetModel =
        MeetRequestAssembler.toModel(request).let { meetFromRequest ->
            meetService.save(
                meetFromRequest.copy(
                    assistants = request.assistants!!
                        .map { personRetrofitClient.findById(it) }
                        .map { AssistantModel(information = it.asShort()) },
                    establishment = establishmentRetrofitClient.findById(request.establishmentId!!)
                )
            )
        }

    override fun qualify(id: String, qualification: String): MeetModel =
        qualification
            .apply {
                Validator.checkTrue(
                    NumberUtils.isDigits(this),
                    "Invalid [${AssistantModel::qualification.name}]"
                )
            }.toInt().let { numericQualification ->
                Validator.checkTrue(
                    QualificationType.values()
                        .any { qualificationType -> qualificationType.value == numericQualification },
                    "Invalid [${AssistantModel::qualification.name}]. " +
                            "It must be between the following values: ${QualificationType.values()}",
                    "invalidQualification"
                )
                meetService.save(
                    meetService.findById(id).let { meet ->
                        meet.copy(
                            assistants = meet.assistants.map { assistant ->
                                if (assistant.information?.id == SecurityContext.getUser().id) {
                                    assistant.copy(qualification = numericQualification)
                                } else assistant
                            }
                        )
                    }
                )
            }

}