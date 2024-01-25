package com.ulikme.interaction.api.facade.internal

import com.ulikme.interaction.api.facade.BlockingFacade
import com.ulikme.interaction.domain.models.BlockingModel
import com.ulikme.interaction.infrastructure.communication.rest.client.PersonRetrofitClient
import com.ulikme.interaction.service.BlockingService
import com.ulikme.utils.http.support.SecurityContext
import org.springframework.stereotype.Service

@Service
open class DefaultBlockingFacade(
    private val blockingService: BlockingService,
    private val personRetrofitClient: PersonRetrofitClient
) : BlockingFacade {

    override fun block(blockedPersonId: String): BlockingModel =
        personRetrofitClient.findById(blockedPersonId).let { personToBlocked ->
            blockingService.block(
                BlockingModel(
                    personId = SecurityContext.getUser().id!!,
                    blockedPerson = personToBlocked
                )
            )
        }

}