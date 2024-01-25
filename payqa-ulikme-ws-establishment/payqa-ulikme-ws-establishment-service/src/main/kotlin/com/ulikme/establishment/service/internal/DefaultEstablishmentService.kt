package com.ulikme.establishment.service.internal

import com.ulikme.establishment.domain.models.EstablishmentModel
import com.ulikme.establishment.infrastructure.persistence.mongo.mappers.EstablishmentModelMapper
import com.ulikme.establishment.infrastructure.persistence.mongo.repositories.EstablishmentRepository
import com.ulikme.establishment.service.EstablishmentService
import dev.payqa.scaffolding.apicrud.design.exceptions.http.NotFoundException
import dev.payqa.scaffolding.apicrud.design.models.Page
import dev.payqa.scaffolding.apicrud.design.rest.rr.request.PaginateRequest
import org.springframework.stereotype.Service

@Service
open class DefaultEstablishmentService(
    private val repository: EstablishmentRepository
) : EstablishmentService {

    override fun paginate(request: PaginateRequest<EstablishmentModel>): Page<EstablishmentModel> =
        repository.findAll(
            request.forRepository(EstablishmentModel::class.java)
        ).let { page -> Page.of(page.totalElements, EstablishmentModelMapper.inverseMap(page.content)) }

    override fun paginateByCountry(request: PaginateRequest<EstablishmentModel>): Page<EstablishmentModel> =
        repository.findAllByCountryCode(
            request.params["countryCode"] as String,
            request.forRepository(EstablishmentModel::class.java)
        ).let { page -> Page.of(page.totalElements, EstablishmentModelMapper.inverseMap(page.content)) }

    override fun paginateByLocation(request: PaginateRequest<EstablishmentModel>): Page<EstablishmentModel>  =
        repository.findAllNearByLatitudeAndLongitude(
            request.params["latitude"] as Double,
            request.params["longitude"] as Double,
            request.forRepository(EstablishmentModel::class.java)
        ).let { page -> Page.of(page.totalElements, EstablishmentModelMapper.inverseMap(page.content)) }

    override fun findById(id: String): EstablishmentModel =
        repository.findById(id)
            .orElseThrow { throw NotFoundException("Cannot find establishment with id: $id") }
            .let { EstablishmentModelMapper.inverseMap(it) }

    override fun save(establishment: EstablishmentModel): EstablishmentModel =
        EstablishmentModelMapper.inverseMap(
            repository.save(
                EstablishmentModelMapper.map(establishment)
            )
        )

    override fun delete(id: String) = repository.deleteById(id)

}