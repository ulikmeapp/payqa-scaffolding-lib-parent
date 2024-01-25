package com.ulikme.person.service

import com.ulikme.person.domain.models.InterestModel

interface InterestService {

    fun listWhichShowInOnBoarding(): List<InterestModel>

    fun findById(id: String): InterestModel

}