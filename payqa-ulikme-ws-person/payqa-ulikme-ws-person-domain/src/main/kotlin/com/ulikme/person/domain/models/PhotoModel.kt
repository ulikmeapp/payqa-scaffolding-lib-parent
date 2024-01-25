package com.ulikme.person.domain.models

import com.fasterxml.jackson.annotation.JsonIgnore
import dev.payqa.scaffolding.apicrud.design.models.Model

const val BLUR_IMAGE_FOLDER = "common"
const val BLUR_IMAGE_NAME = "blur-image.jpg"

data class PhotoModel(
    val id: String = "",
    @JsonIgnore
    val personId: String = "",
    val url: String = "",
    @JsonIgnore
    val key: String = "",
    @JsonIgnore
    val filename: String = "",
    val main: Boolean = false
) : Model()