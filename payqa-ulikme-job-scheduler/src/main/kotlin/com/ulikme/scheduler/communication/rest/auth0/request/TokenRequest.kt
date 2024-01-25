package com.ulikme.scheduler.communication.rest.auth0.request

import com.fasterxml.jackson.annotation.JsonProperty

data class TokenRequest(
    @JsonProperty("client_id")
    val client_id: String,
    @JsonProperty("client_secret")
    val client_secret: String,
    val audience: String,
    @JsonProperty("grant_type")
    val grant_type: String
)