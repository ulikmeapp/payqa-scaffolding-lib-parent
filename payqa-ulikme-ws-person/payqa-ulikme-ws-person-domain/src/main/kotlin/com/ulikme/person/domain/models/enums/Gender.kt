package com.ulikme.person.domain.models.enums

enum class Gender(val key: String) {

    MALE("M"),
    FEMALE("F"),
    OTHER("O");

    companion object {
        fun valueOfKey(key: String): Gender? = values().firstOrNull { it.key == key }
    }

}