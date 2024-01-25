package dev.payqa.scaffolding.apicrud.design.rest.rr.response.spring

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class ResponseEntityCreated<T> private constructor(body: T) : ResponseEntity<T>(body, HttpStatus.CREATED) {

    companion object {
        @JvmStatic
        fun <T> build(body: T): ResponseEntityCreated<T> = ResponseEntityCreated(body)
    }

}