package com.ulikme.interest.service

import com.ulikme.interest.domain.models.InterestModel

interface InterestService {

    fun list(): List<InterestModel>

    fun listWillShowInOnBoarding(): List<InterestModel>

    fun findById(id: String): InterestModel

}