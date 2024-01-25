package com.ulikme.establishment.domain.models

import com.ulikme.country.domain.models.CountryModel
import dev.payqa.scaffolding.apicrud.design.models.Model
import java.math.BigDecimal

data class EstablishmentModel(
    val id: String = "",
    val name: String = "",
    val country: CountryModel = CountryModel(),
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val address: String? = null,
    val schedule: List<String> = emptyList()
) : Model()
