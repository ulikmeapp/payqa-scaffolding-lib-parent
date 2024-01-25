package com.ulikme.chat.domain.enums

enum class MessageType(val key: String, val description: String) {

    SIMPLE("SMP", "Simple message"),
    WITHOUT_MESSAGES("WOM", "Without messages"),
    READY_TO_MEET("RTM", "Ready to meet"),
    MEET_ACCEPTED("MAC", "Meet accepted"),
    MEET_REJECTED("MRJ", "Meet rejected"),
    MEET_COUNTERED("MCT", "Meet countered"),
    MEET_POST_DATE("MPD", "Meet post date"),
    MEET_RATED("MRT", "Meet rated"),
    GROUP_MEET_ACCEPTED("GMA", "Group meet accepted"),
    GROUP_MEET_REJECTED("GMR", "Group meet rejected")

}