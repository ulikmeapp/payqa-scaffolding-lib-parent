package com.ulikme.interaction.domain.rr.request.assembler

import com.ulikme.interaction.domain.enums.InteractionClassification
import com.ulikme.interaction.domain.enums.InteractionType
import com.ulikme.interaction.domain.enums.InteractionZone
import com.ulikme.interaction.domain.models.InteractionModel
import com.ulikme.interaction.domain.rr.request.InteractionRequest
import dev.payqa.scaffolding.apicrud.data.utils.Validator

object InteractionRequestAssembler {

    fun toModel(request: InteractionRequest): InteractionModel {
        Validator.checkNotNull(request.type, InteractionRequest::type.name)
        Validator.checkNotEmpty(request.type, InteractionRequest::type.name)
        Validator.checkNotNull(request.personInteractedId, InteractionRequest::personInteractedId.name)
        Validator.checkNotEmpty(request.personInteractedId, InteractionRequest::personInteractedId.name)
        request.type!!.let { type ->
            Validator.checkTrue(
                InteractionType.values()
                    .any { typeEnum -> typeEnum.value == type },
                "Invalid [${InteractionRequest::type.name}]. " +
                        "It must be between the following values: ${InteractionType.values()}",
                "invalidType"
            )
        }
        request.classification?.let { classification ->
            Validator.checkTrue(
                InteractionClassification.values()
                    .any { classificationEnum -> classificationEnum.value == classification },
                "Invalid [${InteractionRequest::classification.name}]. " +
                        "It must be between the following values: ${InteractionClassification.values()}",
                "invalidClassification"
            )
        }
        request.zone?.let { zone ->
            Validator.checkTrue(
                InteractionZone.values()
                    .any { zoneEnum -> zoneEnum.value == zone },
                "Invalid [${InteractionRequest::zone.name}]. " +
                        "It must be between the following values: ${InteractionClassification.values()}",
                "invalidZone"
            )
        }
        return InteractionModel(
            type = request.type!!,
            classification = request.classification,
            zone = request.zone
        )
    }

}