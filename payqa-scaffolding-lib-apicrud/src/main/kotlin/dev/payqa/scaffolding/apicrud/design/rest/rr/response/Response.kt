package dev.payqa.scaffolding.apicrud.design.rest.rr.response

import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable
import java.util.*

open class Response(

    @JsonIgnore
    val serialId: String = "${UUID.randomUUID()}:${Date().time}"

) : Serializable