package com.ulikme.person.domain.rr.request

import java.math.BigDecimal

data class LocationRequest(
    val latitude: BigDecimal? = null,
    val longitude: BigDecimal? = null
)