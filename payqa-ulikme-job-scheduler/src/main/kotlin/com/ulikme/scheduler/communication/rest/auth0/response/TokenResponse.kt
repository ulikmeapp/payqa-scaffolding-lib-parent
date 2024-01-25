package com.ulikme.scheduler.communication.rest.auth0.response

import com.fasterxml.jackson.annotation.JsonProperty

data class TokenResponse (
    @JsonProperty("access_token")
    val access_token: String = "",
    @JsonProperty("token_type")
    val token_type: String = ""
)