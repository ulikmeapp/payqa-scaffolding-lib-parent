package com.ulikme.person.domain.models.enums

enum class SexualOrientation(val key: String) {

    HETEROSEXUAL("H"),
    GAY("G"),
    LESBIAN("L"),
    BISEXUAL("B"),
    ASEXUAL("A"),
    PANSEXUAL("P"),
    QUEER("Q"),
    HAVE_DOUBTS("X");

    companion object {
        fun valueOfKey(key: String): SexualOrientation? = values().firstOrNull { it.key == key }
    }

}