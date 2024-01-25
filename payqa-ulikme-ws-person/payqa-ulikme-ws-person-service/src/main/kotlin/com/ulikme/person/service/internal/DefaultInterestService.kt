package com.ulikme.person.service.internal

import com.ulikme.person.domain.models.InterestModel
import com.ulikme.person.infrastructure.persistence.mongo.mappers.InterestModelMapper
import com.ulikme.person.infrastructure.persistence.mongo.repositories.InterestRepository
import com.ulikme.person.service.InterestService
import dev.payqa.scaffolding.apicrud.design.exceptions.http.NotFoundException
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
open class DefaultInterestService(
    private val repository: InterestRepository
) : InterestService {

    @Cacheable("interests-on-boarding")
    override fun listWhichShowInOnBoarding(): List<InterestModel> =
        InterestModelMapper.inverseMap(
            repository.findByShowInOnBoarding()
        )

    @Cacheable("interest-by-id", key = "#id")
    override fun findById(id: String): InterestModel =
        InterestModelMapper.inverseMap(
            repository.findById(id)
                .orElseThrow { NotFoundException("Cannot find interest with id: $id") }
        )

}