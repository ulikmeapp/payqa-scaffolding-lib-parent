package com.ulikme.person.domain.rr.request

data class NotificationRequest(
    val content: String? = null,
    val date: Long? = null,
    val type: String? = null,
    val data: Any? = null
)