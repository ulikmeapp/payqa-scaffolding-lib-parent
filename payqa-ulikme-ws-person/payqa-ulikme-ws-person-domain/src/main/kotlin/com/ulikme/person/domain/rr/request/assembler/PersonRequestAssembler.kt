package com.ulikme.person.domain.rr.request.assembler

import com.ulikme.person.domain.models.BehaviorInformation
import com.ulikme.person.domain.models.ComplementaryInformation
import com.ulikme.person.domain.models.PersonModel
import com.ulikme.person.domain.models.PersonalInformation
import com.ulikme.person.domain.models.enums.Gender
import com.ulikme.person.domain.models.enums.SexualOrientation
import com.ulikme.person.domain.rr.request.Behavior
import com.ulikme.person.domain.rr.request.PersonRequest
import com.ulikme.person.domain.rr.request.Personal
import com.ulikme.utils.common.DateTimeUtils
import dev.payqa.scaffolding.apicrud.data.exceptions.ValidationException
import dev.payqa.scaffolding.apicrud.data.utils.Validator

object PersonRequestAssembler {

    fun toRegisterModel(request: PersonRequest): PersonModel {
        Validator.checkNotNull(request.personal, PersonRequest::personal.name)
        Validator.checkNotNull(request.personal!!.name, Personal::name.name)
        Validator.checkNotEmpty(request.personal.name, Personal::name.name)
        Validator.checkNotNull(request.personal.email, Personal::email.name)
        Validator.checkNotEmpty(request.personal.email, Personal::email.name)
        validateOptionalValues(request)
        return PersonModel(
            personal = PersonalInformation(
                name = request.personal!!.name!!,
                lastName = request.personal.lastName,
                mobilePhone = request.personal.mobilePhone,
                email = request.personal.email!!,
                bornDate = request.personal.bornDate
            ),
            behavior = BehaviorInformation(
                gender = request.behavior?.gender,
                orientations = request.behavior?.orientations ?: emptyList()
            ),
            complementary = ComplementaryInformation(
                shortDescription = request.complementary?.shortDescription,
                workPlace = request.complementary?.workPlace,
                whereLive = request.complementary?.whereLive
            )
        )
    }

    fun toUpdateModel(request: PersonRequest, currentPerson: PersonModel): PersonModel {
        validateOptionalValues(request)
        return PersonModel(
            id = currentPerson.id,
            personal = PersonalInformation(
                name = request.personal?.name ?: currentPerson.personal.name,
                lastName = request.personal?.lastName ?: currentPerson.personal.lastName,
                mobilePhone = request.personal?.mobilePhone ?: currentPerson.personal.mobilePhone,
                email = request.personal?.email ?: currentPerson.personal.email,
                bornDate = request.personal?.bornDate ?: currentPerson.personal.bornDate
            ),
            behavior = currentPerson.behavior.copy(
                gender = request.behavior?.gender ?: currentPerson.behavior.gender,
                orientations = request.behavior?.orientations ?: currentPerson.behavior.orientations
            ),
            complementary = ComplementaryInformation(
                shortDescription = request.complementary?.shortDescription ?: currentPerson.complementary.shortDescription,
                workPlace = request.complementary?.workPlace ?: currentPerson.complementary.workPlace,
                whereLive = request.complementary?.whereLive ?: currentPerson.complementary.whereLive,
                professionalRole = request.complementary?.professionalRole ?: currentPerson.complementary.professionalRole,
                picture = request.complementary?.picture ?: currentPerson.complementary.picture
            )
        )

    }

    private fun validateOptionalValues(request: PersonRequest) = with(request) {
        personal?.email?.let { email ->
            Validator.checkTrue(
                Regex("^(.+)@(.+)\$").matches(email),
                "Invalid [${Personal::email.name}]",
                "invalidEmail"
            )
        }
        personal?.bornDate?.let { bornDate ->
            DateTimeUtils.parse(bornDate)
                ?: throw ValidationException(key = "invalidBornDate", "Invalid [${Personal::bornDate.name}]")
        }
        behavior?.gender?.let { gender ->
            Validator.checkTrue(
                Gender.values().any { genderEnum -> genderEnum.key == gender },
                "Invalid [${Behavior::gender.name}]. " +
                        "It must be between the following values: ${Gender.values().contentToString()}",
                "invalidGender"
            )
        }
        behavior?.orientations?.let { orientations ->
            Validator.checkTrue(
                orientations.firstOrNull { orientation ->
                    SexualOrientation.values().any { orientationEnum -> orientationEnum.key == orientation }
                } != null,
                "Invalid [${Behavior::orientations.name}]. " +
                        "Them must be between the following values: ${SexualOrientation.values().contentToString()}",
                "invalidOrientations"
            )
        }
    }

}