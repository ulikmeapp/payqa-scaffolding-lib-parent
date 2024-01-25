package com.ulikme.interest.domain.models

import dev.payqa.scaffolding.apicrud.design.models.Model

data class InterestModel(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val showInOnBoarding: Boolean = false
) : Model()
