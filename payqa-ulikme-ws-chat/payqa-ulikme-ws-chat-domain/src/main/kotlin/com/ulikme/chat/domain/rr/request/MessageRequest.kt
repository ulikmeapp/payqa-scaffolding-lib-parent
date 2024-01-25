package com.ulikme.chat.domain.rr.request

data class MessageRequest(
    var content: String? = null,
    var type: String? = null,
    var meet: MeetInMessageRequest? = null
)

data class MeetInMessageRequest(
    var establishmentId: String? = null,
    var date: String? = null,
    var hour: String? = null,
    var proposeId: String? = null
)
