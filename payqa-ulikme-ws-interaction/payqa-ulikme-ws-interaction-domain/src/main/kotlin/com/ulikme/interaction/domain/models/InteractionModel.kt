package com.ulikme.interaction.domain.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.ulikme.person.domain.models.PersonModel
import dev.payqa.scaffolding.apicrud.design.models.Model
import java.util.*

data class InteractionModel(
    val id: String = "",
    val type: String = "",
    val classification: String? = null,
    val zone: String? = null,
    val personInteracted: PersonModel? = null,
    val match: Boolean = false,
    val inGroup: Boolean = false,
    @JsonIgnore
    val createdPersonId: String? = null,
    @JsonIgnore
    val createdDate: Date? = null
) : Model()
