package com.ulikme.country.api.rest

import com.ulikme.country.domain.models.CountryModel
import com.ulikme.country.service.CountryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/country")
open class CountryController(
    private val countryService: CountryService
) {

    @GetMapping("/{code}")
    fun findByCode(
        @PathVariable code: String
    ): ResponseEntity<CountryModel> =
        ResponseEntity.ok(countryService.findByCode(code))

}