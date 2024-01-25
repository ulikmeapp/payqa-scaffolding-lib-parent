package com.ulikme.person.domain.rr.request.assembler

import com.ulikme.person.domain.models.FullPersonModel
import com.ulikme.person.domain.rr.request.SearchRequest
import java.math.BigDecimal

object SearchRequestAssembler {

    fun fromFullPerson(fullPerson: FullPersonModel) =
        SearchRequest(
            lat = BigDecimal(fullPerson.latestLocation!!.latitude),
            lng = BigDecimal(fullPerson.latestLocation!!.longitude),
            dst = fullPerson.preferences.maxDistance,
            sm = fullPerson.preferences.showMe,
            age = fullPerson.preferences.maxAge,
            minAge=fullPerson.preferences.minAge,
        )

}