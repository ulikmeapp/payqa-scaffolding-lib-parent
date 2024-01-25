package com.ulikme.person.domain.models

import com.ulikme.country.domain.models.CountryModel
import com.ulikme.person.domain.annotations.ProfileAttribute
import com.ulikme.person.domain.common.ShortPerson
import dev.payqa.scaffolding.apicrud.design.models.Model
import dev.payqa.scaffolding.utils.common.DateUtils
import dev.payqa.scaffolding.utils.common.enums.DateFormat
import org.slf4j.LoggerFactory
import java.lang.reflect.Field
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.util.*

data class PersonModel(
    val id: String = "",
    val personal: PersonalInformation = PersonalInformation(),
    val behavior: BehaviorInformation = BehaviorInformation(),
    val complementary: ComplementaryInformation = ComplementaryInformation(),
    val subscription: SubscriptionInformation = SubscriptionInformation()
) : Model() {

    var profilePercentage: Int = 0
        private set

    companion object {
        private val log = LoggerFactory.getLogger(PersonModel::class.java)
    }

    constructor(
        id: String = "",
        personal: PersonalInformation = PersonalInformation(),
        behavior: BehaviorInformation = BehaviorInformation(),
        complementary: ComplementaryInformation = ComplementaryInformation(),
        subscription: SubscriptionInformation = SubscriptionInformation(),
        profilePercentage: Int = 0
    ) : this(id, personal, behavior, complementary, subscription) {
        this.profilePercentage = profilePercentage
    }

    fun calculateProfilePercentage() {
        profilePercentage = with(mutableListOf<Field>()) {
            addAll(this@PersonModel.personal.javaClass.declaredFields
                .filter { it.annotations.any { annotation -> annotation is ProfileAttribute } }
                .filter {
                    it.isAccessible = true
                    hasValue(it.get(this@PersonModel.personal))
                })
            addAll(this@PersonModel.behavior.javaClass.declaredFields
                .filter { it.annotations.any { annotation -> annotation is ProfileAttribute } }
                .filter {
                    it.isAccessible = true
                    hasValue(it.get(this@PersonModel.behavior))
                })
            addAll(this@PersonModel.complementary.javaClass.declaredFields
                .filter { it.annotations.any { annotation -> annotation is ProfileAttribute } }
                .filter {
                    it.isAccessible = true
                    hasValue(it.get(this@PersonModel.complementary))
                })
            this
        }.sumOf { it.getAnnotation(ProfileAttribute::class.java).percentage }
    }

    fun asShort(): ShortPerson = ShortPerson(
        id,
        personal.name,
        complementary.picture,
        behavior.gender,
        personal.age
    )

    private fun hasValue(fieldValue: Any?): Boolean =
        fieldValue?.let { value ->
            when (value) {
                is List<*> -> value.isNotEmpty()
                is Boolean -> value
                else -> Objects.nonNull(value)
            }
        } ?: false

}

data class PersonalInformation(
    val name: String = "",

    @ProfileAttribute(percentage = 2)
    val lastName: String? = null,

    @ProfileAttribute(percentage = 10)
    val mobilePhone: String? = null,

    val email: String = "",

    @ProfileAttribute(percentage = 10)
    val bornDate: String? = null,

    @ProfileAttribute(percentage = 10)
    val country: CountryModel? = null
) {

    var age: Int? = null
        private set

    init { calculateAge() }

    fun calculateAge() {
        age = bornDate
            ?.let { DateUtils.parse(it, DateFormat.ENGLISH_DATE) }
            ?.let {
                Period.between(
                    it.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    LocalDate.now()
                ).years
            }
    }

}

data class BehaviorInformation(
    @ProfileAttribute(percentage = 20)
    val interests: List<InterestModel> = emptyList(),

    @ProfileAttribute
    val gender: String? = null,

    @ProfileAttribute
    val orientations: List<String> = emptyList(),

    @ProfileAttribute
    val mobilePhoneVerified: Boolean = false,

    @ProfileAttribute
    val emailVerified: Boolean = false,

    @ProfileAttribute(percentage = 30)
    val profileVerified: Boolean = false
)

data class ComplementaryInformation(
    @ProfileAttribute
    val shortDescription: String? = null,

    @ProfileAttribute
    val workPlace: String? = null,

    @ProfileAttribute
    val whereLive: String? = null,

    @ProfileAttribute
    val professionalRole: String? = null,

    @ProfileAttribute(percentage = 10)
    val picture: String? = null
)

data class SubscriptionInformation(
    val subscribed: Boolean = false,
    val promoted: Boolean = false,
    val promotedDate: String? = null,
    val promotedTimeZone: String? = null
)