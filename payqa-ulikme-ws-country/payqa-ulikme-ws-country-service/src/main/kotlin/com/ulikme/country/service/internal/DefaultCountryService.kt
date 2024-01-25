package com.ulikme.country.service.internal

import com.ulikme.country.domain.models.CountryModel
import com.ulikme.country.infrastructure.persistence.mongo.mappers.CountryModelMapper
import com.ulikme.country.infrastructure.persistence.mongo.repositories.CountryRepository
import com.ulikme.country.service.CountryService
import dev.payqa.scaffolding.apicrud.design.exceptions.http.NotFoundException
import org.springframework.stereotype.Service

@Service
open class DefaultCountryService(
    private val repository: CountryRepository
) : CountryService {

    override fun findByCode(code: String): CountryModel =
        CountryModelMapper.inverseMap(
            repository.findByCode(code)
                .orElseThrow { NotFoundException("Cannot find country with code: $code") }
        )

    override fun findByCodeOrNull(code: String): CountryModel? =
        repository.findByCode(code).orElse(null)?.let { CountryModelMapper.inverseMap(it) }

}