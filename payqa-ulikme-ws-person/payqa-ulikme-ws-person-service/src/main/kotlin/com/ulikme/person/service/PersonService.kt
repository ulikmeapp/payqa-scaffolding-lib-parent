package com.ulikme.person.service

import com.ulikme.person.domain.models.PersonModel

interface PersonService {

    fun findById(id: String): PersonModel

    fun findByEmail(email: String): PersonModel

    fun findByEmailOrNull(email: String): PersonModel?

    fun save(person: PersonModel): PersonModel

    fun delete(id: String)

    fun deleteByEmail(email: String)

}