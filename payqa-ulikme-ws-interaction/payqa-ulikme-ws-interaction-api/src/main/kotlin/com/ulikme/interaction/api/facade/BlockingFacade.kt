package com.ulikme.interaction.api.facade

import com.ulikme.interaction.domain.models.BlockingModel

interface BlockingFacade {

    fun block(blockedPersonId: String): BlockingModel

}