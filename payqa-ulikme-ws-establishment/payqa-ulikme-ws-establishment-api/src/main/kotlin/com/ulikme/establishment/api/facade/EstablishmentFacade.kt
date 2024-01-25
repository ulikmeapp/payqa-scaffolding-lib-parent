package com.ulikme.establishment.api.facade

import com.ulikme.establishment.domain.models.EstablishmentModel
import com.ulikme.establishment.domain.rr.request.EstablishmentRequest

interface EstablishmentFacade {

    fun register(request: EstablishmentRequest): EstablishmentModel

    fun update(id: String, request: EstablishmentRequest): EstablishmentModel

}