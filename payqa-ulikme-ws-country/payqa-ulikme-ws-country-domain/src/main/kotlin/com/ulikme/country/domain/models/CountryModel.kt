package com.ulikme.country.domain.models

import dev.payqa.scaffolding.apicrud.design.models.Model

const val DEFAULT_COUNTRY = "US"

data class CountryModel(
    val code: String = "",
    val name: String = "",
    val phoneCode: String = ""
) : Model()
