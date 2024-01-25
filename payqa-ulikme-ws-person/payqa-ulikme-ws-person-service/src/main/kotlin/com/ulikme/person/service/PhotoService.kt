package com.ulikme.person.service

import com.ulikme.person.domain.models.PhotoModel

interface PhotoService {

    fun listByPerson(personId: String, verification: Boolean): List<PhotoModel>

    fun findMainByPersonOrNull(personId: String): PhotoModel?

    fun findByPersonAndNameOrNull(personId: String, name: String): PhotoModel?

    fun register(photo: PhotoModel ,isVerification: Boolean = false): PhotoModel

    fun delete(id: String)

    fun deleteByName(personId: String, name: String)

}