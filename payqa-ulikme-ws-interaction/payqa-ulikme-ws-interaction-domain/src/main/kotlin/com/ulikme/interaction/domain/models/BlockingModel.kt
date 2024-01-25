package com.ulikme.interaction.domain.models

import com.ulikme.person.domain.models.PersonModel
import dev.payqa.scaffolding.apicrud.design.models.Model

data class BlockingModel(
    val personId: String = "",
    val blockedPerson: PersonModel? = null
) : Model()