package com.ulikme.person.domain.models

import com.fasterxml.jackson.annotation.JsonIgnore
import dev.payqa.scaffolding.apicrud.design.models.Model
import java.math.BigDecimal

data class LocationModel(
    val id: String = "",
    @JsonIgnore
    val personId: String = "",
    val latitude: Double = BigDecimal.ZERO.toDouble(),
    val longitude: Double = BigDecimal.ZERO.toDouble()
) : Model()