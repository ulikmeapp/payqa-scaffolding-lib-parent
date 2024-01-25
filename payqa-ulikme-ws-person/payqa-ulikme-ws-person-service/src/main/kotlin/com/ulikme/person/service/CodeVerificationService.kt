package com.ulikme.person.service

interface CodeVerificationService {

    fun findLatestByPersonId(personId: String): String

    fun register(personId: String, type: String, code: String): String

}