package dev.payqa.scaffolding.apicrud.design.rest.rr.response.spring

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class ResponseEntityNoContent private constructor() : ResponseEntity<Void>(HttpStatus.NO_CONTENT) {

    companion object {
        @JvmStatic
        fun build() = ResponseEntityNoContent()
    }

}