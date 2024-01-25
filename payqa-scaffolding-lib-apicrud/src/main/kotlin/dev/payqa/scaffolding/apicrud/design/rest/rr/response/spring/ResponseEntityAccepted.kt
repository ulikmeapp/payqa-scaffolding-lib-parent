package dev.payqa.scaffolding.apicrud.design.rest.rr.response.spring

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class ResponseEntityAccepted private constructor() : ResponseEntity<Void>(HttpStatus.ACCEPTED) {

    companion object {
        @JvmStatic
        fun build() = ResponseEntityAccepted()
    }

}