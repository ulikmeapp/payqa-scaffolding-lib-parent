package com.ulikme.person.service

import com.ulikme.person.domain.models.PreferencesModel

interface PreferencesService {

    fun findByPersonId(personId: String): PreferencesModel

    fun findByPersonIdOrNull(personId: String): PreferencesModel?

    fun save(personId: String, preferences: PreferencesModel): PreferencesModel
}