package com.ulikme.person.domain.rr.response

import dev.payqa.scaffolding.apicrud.design.rest.rr.response.Response

data class VerifyResponse(
    val success: Boolean = false,
    val message: String? = null
) : Response()
