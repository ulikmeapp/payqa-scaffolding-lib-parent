package com.ulikme.meet.domain.rr.request

data class MeetRequest(
    val chatId: String? = null,
    val date: String? = null,
    val assistants: List<String>? = null,
    val establishmentId: String? = null
)
