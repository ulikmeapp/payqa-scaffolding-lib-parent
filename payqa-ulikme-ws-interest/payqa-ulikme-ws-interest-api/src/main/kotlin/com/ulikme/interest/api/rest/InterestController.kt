package com.ulikme.interest.api.rest

import com.ulikme.interest.domain.models.InterestModel
import com.ulikme.interest.service.InterestService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/interest")
open class InterestController(
    private val interestService: InterestService
) {

    @GetMapping
    fun list(@RequestParam(required = false) showInOnBoarding: Boolean = false): ResponseEntity<List<InterestModel>> =
        ResponseEntity.ok(
            when (showInOnBoarding) {
                true -> interestService.listWillShowInOnBoarding()
                false -> interestService.list()
            }
        )

}