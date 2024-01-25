package com.ulikme.interaction.api.facade

import com.ulikme.interaction.domain.models.InteractionModel
import com.ulikme.interaction.domain.rr.request.InteractionRequest

interface InteractionFacade {

    fun register(request: InteractionRequest): InteractionModel

}