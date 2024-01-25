package com.ulikme.chat.domain.enums

enum class ChatStatus(
    val key: String,
    val description: String
) {

    OPENED("O", "Opened"),
    READY_TO_MEET("R", "Ready to meet"),
    AFTER_RATE("A", "After rate"),
    ELEVATED("E", "Elevated to group meeting"),
    FINISHED("F", "Finished");

}