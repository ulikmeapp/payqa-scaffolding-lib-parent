package com.ulikme.interaction.domain.rr.request

data class InteractionRequest(
    var type: String? = null,
    var classification: String? = null,
    var zone: String? = null,
    var personInteractedId: String? = null
)
