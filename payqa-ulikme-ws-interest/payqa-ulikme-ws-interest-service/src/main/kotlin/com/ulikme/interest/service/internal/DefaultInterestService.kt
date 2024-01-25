package com.ulikme.interest.service.internal

import com.ulikme.interest.domain.models.InterestModel
import com.ulikme.interest.infrastructure.persistence.mongo.mappers.InterestModelMapper
import com.ulikme.interest.infrastructure.persistence.mongo.repositories.InterestRepository
import com.ulikme.interest.service.InterestService
import dev.payqa.scaffolding.apicrud.design.exceptions.http.NotFoundException
import org.springframework.stereotype.Service

@Service
open class DefaultInterestService(
    private val repository: InterestRepository
) : InterestService {

    override fun list(): List<InterestModel> =
        InterestModelMapper.inverseMap(
            repository.findAll()
        )

    override fun listWillShowInOnBoarding(): List<InterestModel> =
        InterestModelMapper.inverseMap(
            repository.findByShowInOnBoarding()
        )

    override fun findById(id: String): InterestModel =
        InterestModelMapper.inverseMap(
            repository.findById(id)
                .orElseThrow { NotFoundException("Cannot find interest with id: $id") }
        )

}