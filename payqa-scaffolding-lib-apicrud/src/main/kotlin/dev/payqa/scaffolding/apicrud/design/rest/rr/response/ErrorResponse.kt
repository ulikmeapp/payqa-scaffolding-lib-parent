package dev.payqa.scaffolding.apicrud.design.rest.rr.response

data class ErrorResponse(

    val code: Int,
    val key: String? = null,
    val message: String?

)