package com.ulikme.chat.domain.rr.request

data class ChatRequest(
    var participants: List<String>? = null,
    val interactionId: String? = null
)
