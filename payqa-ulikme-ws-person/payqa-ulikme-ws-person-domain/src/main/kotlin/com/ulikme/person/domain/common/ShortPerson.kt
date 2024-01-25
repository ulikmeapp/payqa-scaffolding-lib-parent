package com.ulikme.person.domain.common

/**
 * This class must be used as attribute where ever wants to
 * represent just short information about a person.
 * It's recommendable update it into collection where it will
 * be used, through a whatever queue service.
 */
open class ShortPerson(
    open val id: String = "",
    open val name: String = "",
    open val picture: String? = null,
    open val gender: String? = null,
    open val age: Int? = null
)
