package com.ulikme.establishment.service

import com.ulikme.establishment.domain.models.EstablishmentModel
import dev.payqa.scaffolding.apicrud.design.models.Page
import dev.payqa.scaffolding.apicrud.design.rest.rr.request.PaginateRequest

interface EstablishmentService {

    fun paginate(request: PaginateRequest<EstablishmentModel>): Page<EstablishmentModel>

    fun paginateByCountry(
        request: PaginateRequest<EstablishmentModel>
    ): Page<EstablishmentModel>

    fun paginateByLocation(
        request: PaginateRequest<EstablishmentModel>
    ): Page<EstablishmentModel>

    fun findById(id: String): EstablishmentModel

    fun save(establishment: EstablishmentModel): EstablishmentModel

    fun delete(id: String)

}