package com.ulikme.person.api.facade.internal

import com.ulikme.person.api.facade.PersonFacade
import com.ulikme.person.domain.models.PersonModel
import com.ulikme.person.domain.models.PreferencesModel
import com.ulikme.person.domain.rr.request.PersonRequest
import com.ulikme.person.domain.rr.request.PreferencesRequest
import com.ulikme.person.domain.rr.request.assembler.PersonRequestAssembler
import com.ulikme.person.domain.rr.request.assembler.PreferencesRequestAssembler
import com.ulikme.person.infrastructure.communication.rest.client.CountryRetrofitClient
import com.ulikme.person.service.FullPersonService
import com.ulikme.person.service.InterestService
import com.ulikme.person.service.PersonService
import com.ulikme.person.service.PreferencesService
import com.ulikme.utils.http.support.SecurityContext
import dev.payqa.scaffolding.apicrud.data.exceptions.ValidationException
import org.springframework.stereotype.Service

@Service
open class DefaultPersonFacade(
    private val personService: PersonService,
    private val fullPersonService: FullPersonService,
    private val interestService: InterestService,
    private val preferencesService: PreferencesService,
    private val countryRetrofitClient: CountryRetrofitClient
) : PersonFacade {

    override fun register(request: PersonRequest): PersonModel =
        personService.save(
            PersonRequestAssembler.toRegisterModel(request).let { personFromRequest ->
                personService.findByEmailOrNull(personFromRequest.personal.email)?.let {
                    throw ValidationException(
                        key = "emailAlreadyExists",
                        "Already exists a person with email: ${personFromRequest.personal.email}"
                    )
                }

                personFromRequest.copy(
                    id = SecurityContext.getUser().id ?: "",
                    personal = personFromRequest.personal.copy(
                        country = request.personal?.country?.let { countryRetrofitClient.findByCode(it) }
                    ),
                    behavior = personFromRequest.behavior.copy(
                        interests = request.behavior?.interests?.map { interestService.findById(it) } ?: emptyList()
                    )
                )
            }
        ).also { personRegistered ->
            val preferences = preferencesService.save(personRegistered.id, PreferencesModel())
            fullPersonService.save(personRegistered, preferences = preferences)
        }

    override fun update(id: String, request: PersonRequest): PersonModel {
        request.personal?.email?.let { email ->
            personService.findByEmailOrNull(email)?.let {
                if (it.id != id) {
                    throw ValidationException(
                        key = "emailAlreadyExists",
                        "Already exists a person with email: $email"
                    )
                }
            }
        }

        return personService.findById(id).let { currentPerson ->
            personService.save(
                PersonRequestAssembler.toUpdateModel(request, currentPerson).let { personFromRequest ->
                    personFromRequest.copy(
                        personal = personFromRequest.personal.copy(
                            country = request.personal?.country?.let { countryRetrofitClient.findByCode(it) }
                                ?: currentPerson.personal.country
                        ),
                        behavior = personFromRequest.behavior.copy(
                            interests = request.behavior?.interests?.map { interestService.findById(it) }
                                ?: currentPerson.behavior.interests
                        )
                    )
                }
            )
        }.also { fullPersonService.save(it) }
    }

    override fun updatePreferences(id: String, request: PreferencesRequest): PreferencesModel =
        preferencesService.findByPersonId(id).let { currentPreferences ->
            preferencesService.save(id, PreferencesRequestAssembler.toModel(request, currentPreferences))
        }.also { fullPersonService.updatePreferences(id, it) }

}