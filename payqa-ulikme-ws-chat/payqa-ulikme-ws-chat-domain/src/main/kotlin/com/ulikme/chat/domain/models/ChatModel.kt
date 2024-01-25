package com.ulikme.chat.domain.models

import com.ulikme.interaction.domain.models.InteractionModel
import com.ulikme.person.domain.common.ShortPerson
import dev.payqa.scaffolding.apicrud.design.models.Model

data class ChatModel(
    val id: String = "",
    val participants: List<ShortPerson> = emptyList(),
    val interaction: InteractionModel? = null,
    val status: String = "",
    val latestMessage: MessageModel? = null,
    val latestMessages: List<MessageModel> = emptyList()
) : Model() {

    var group: Boolean = participants.size > 2
        private set

}