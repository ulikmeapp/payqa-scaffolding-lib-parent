package com.ulikme.person.infrastructure.persistence.mongo.mappers

import com.ulikme.country.infrastructure.persistence.mongo.mappers.CountryModelMapper
import com.ulikme.person.domain.models.*
import com.ulikme.person.infrastructure.persistence.mongo.entities.PersonEntity
import dev.payqa.scaffolding.apicrud.design.models.mapper.ModelMapper
import dev.payqa.scaffolding.utils.common.DateUtils
import dev.payqa.scaffolding.utils.common.enums.DateFormat
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId

object PersonModelMapper : ModelMapper<PersonModel, PersonEntity>() {

    override fun map(input: PersonModel): PersonEntity =
        PersonEntity(
            id = input.id.takeIf { it.isNotBlank() },
            name = input.personal.name,
            lastName = input.personal.lastName,
            mobilePhone = input.personal.mobilePhone,
            email = input.personal.email,
            bornDate = input.personal.bornDate,
            age = input.apply { personal.calculateAge() }.personal.age,
            country = input.personal.country?.let { CountryModelMapper.map(it) },
            interests = InterestModelMapper.map(input.behavior.interests),
            gender = input.behavior.gender,
            orientations = input.behavior.orientations,
            mobilePhoneVerified = input.behavior.mobilePhoneVerified,
            emailVerified = input.behavior.emailVerified,
            profileVerified = input.behavior.profileVerified,
            shortDescription = input.complementary.shortDescription,
            workPlace = input.complementary.workPlace,
            whereLive = input.complementary.whereLive,
            professionalRole = input.complementary.professionalRole,
            picture = input.complementary.picture,
            subscribed = input.subscription.subscribed,
            promoted = input.subscription.promoted,
            promotedDate = input.subscription.promotedDate,
            promotedTimeZone = input.subscription.promotedTimeZone,
            profilePercentage = input.profilePercentage,
        )

    override fun inverseMap(input: PersonEntity): PersonModel =
        PersonModel(
            id = input.id ?: "",
            personal = PersonalInformation(
                name = input.name,
                lastName = input.lastName,
                mobilePhone = input.mobilePhone,
                email = input.email,
                bornDate = input.bornDate,
                country = input.country?.let { CountryModelMapper.inverseMap(it) }
            ),
            behavior = BehaviorInformation(
                interests = InterestModelMapper.inverseMap(input.interests),
                gender = input.gender,
                orientations = input.orientations,
                mobilePhoneVerified = input.mobilePhoneVerified,
                emailVerified = input.emailVerified,
                profileVerified = input.profileVerified
            ),
            complementary = ComplementaryInformation(
                shortDescription = input.shortDescription,
                workPlace = input.workPlace,
                whereLive = input.whereLive,
                professionalRole = input.professionalRole,
                picture = input.picture
            ),
            subscription = SubscriptionInformation(
                subscribed = input.subscribed,
                promoted = input.promoted,
                promotedDate = input.promotedDate,
                promotedTimeZone = input.promotedTimeZone
            ),
            profilePercentage = input.profilePercentage
        ).apply { personal.calculateAge() }

}