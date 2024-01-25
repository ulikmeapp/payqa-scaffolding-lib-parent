package com.ulikme.person.api.facade

import com.ulikme.person.domain.models.PersonModel
import com.ulikme.person.domain.models.PreferencesModel
import com.ulikme.person.domain.rr.request.PersonRequest
import com.ulikme.person.domain.rr.request.PreferencesRequest

interface PersonFacade {

    fun register(request: PersonRequest): PersonModel

    fun update(id: String, request: PersonRequest): PersonModel

    fun updatePreferences(id: String, request: PreferencesRequest): PreferencesModel

}