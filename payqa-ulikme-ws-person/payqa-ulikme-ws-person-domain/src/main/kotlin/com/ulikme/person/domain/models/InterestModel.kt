package com.ulikme.person.domain.models

import dev.payqa.scaffolding.apicrud.design.models.Model

// TODO: Move this class to their own microservice
data class InterestModel(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val showInOnBoarding: Boolean = false
) : Model()