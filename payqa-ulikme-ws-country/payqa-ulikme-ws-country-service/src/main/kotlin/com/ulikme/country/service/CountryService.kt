package com.ulikme.country.service

import com.ulikme.country.domain.models.CountryModel

interface CountryService {

    fun findByCode(code: String): CountryModel

    fun findByCodeOrNull(code: String): CountryModel?

}