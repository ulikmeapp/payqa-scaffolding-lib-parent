package com.ulikme.person.domain.models

import com.fasterxml.jackson.databind.ObjectMapper
import dev.payqa.scaffolding.apicrud.design.models.Model
import software.amazon.awssdk.services.sqs.model.Message
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue
import software.amazon.awssdk.services.sqs.model.MessageSystemAttributeName

/**
 * Default age to use in main search
 */
const val DEFAULT_AGE = 18

/**
 * Default distance to use in main search as sphere (specified in miles)
 */
const val DEFAULT_DISTANCE = 1

/**
 * Identifier to use in main search as a way to retrieve all genders
 */
const val ALL_GENDERS = "A"

data class FullPersonModel(
    val id: String = "",
    val personal: PersonalInformation = PersonalInformation(),
    val behavior: BehaviorInformation = BehaviorInformation(),
    val complementary: ComplementaryInformation = ComplementaryInformation(),
    val subscription: SubscriptionInformation = SubscriptionInformation(),
    val profilePercentage: Int = 0,
    val photos: List<PhotoModel> = emptyList(),
    val preferences: PreferencesModel = PreferencesModel(),
    val latestLocation: LocationModel? = null
) : Model() {

    init { personal.calculateAge() }

    fun asPerson(): PersonModel = PersonModel(id, personal, behavior, complementary, subscription, profilePercentage)

    fun asSqsMessage(): Message =
        Message.builder()
            .body(ObjectMapper().writeValueAsString(this))
            .messageAttributes(
                mutableMapOf(
                    MessageSystemAttributeName.MESSAGE_DEDUPLICATION_ID.toString() to
                            MessageAttributeValue.builder().stringValue(id).build(),
                    MessageSystemAttributeName.MESSAGE_GROUP_ID.toString() to
                            MessageAttributeValue.builder().stringValue(FullPersonModel::class.java.name).build()
                )
            )
            .build()

}
