package com.ulikme.establishment.domain.rr.request

import java.math.BigDecimal

data class EstablishmentRequest(
    val name: String? = null,
    val country: String? = null,
    val latitude: BigDecimal? = null,
    val longitude: BigDecimal? = null,
    val address: String? = null,
    val schedule: List<String>? = null
)