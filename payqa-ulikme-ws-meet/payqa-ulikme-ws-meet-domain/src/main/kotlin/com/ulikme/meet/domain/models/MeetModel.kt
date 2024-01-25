package com.ulikme.meet.domain.models

import com.ulikme.establishment.domain.models.EstablishmentModel
import com.ulikme.meet.domain.enums.MeetStatus
import com.ulikme.person.domain.common.ShortPerson
import dev.payqa.scaffolding.apicrud.design.models.Model

const val SIMPLE_MEET_QUANTITY = 2
const val MINIMUM_GROUP_MEET_QUANTITY = 3
const val MAXIMUM_GROUP_MEET_QUANTITY = 6

data class MeetModel(
    val id: String = "",
    val chatId: String? = null,
    val date: String? = null,
    val assistants: List<AssistantModel> = emptyList(),
    val establishment: EstablishmentModel? = null,
    val group: Boolean = false,
    val status: String = MeetStatus.CONFIRMED.key
) : Model()

data class AssistantModel(
    val information: ShortPerson? = null,
    val qualification: Int? = null,
    val confirmed: Boolean = true
) : Model()