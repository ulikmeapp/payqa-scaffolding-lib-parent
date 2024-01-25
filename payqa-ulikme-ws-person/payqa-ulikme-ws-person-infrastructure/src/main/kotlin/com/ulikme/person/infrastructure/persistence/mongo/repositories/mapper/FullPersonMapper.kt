package com.ulikme.person.infrastructure.persistence.mongo.repositories.mapper

import com.ulikme.country.infrastructure.persistence.mongo.entities.CountryEntity
import com.ulikme.person.infrastructure.persistence.mongo.entities.*
import com.ulikme.utils.data.config.FIELD_ID
import org.bson.Document

object FullPersonMapper {

    fun map(document: Document): FullPersonEntity =
        FullPersonEntity(
            //id = document.getObjectId(FIELD_ID).toString(),
            name = document.getString(FullPersonEntity::name.name),
            lastName = document.getString(FullPersonEntity::lastName.name),
            mobilePhone = document.getString(FullPersonEntity::mobilePhone.name),
            email = document.getString(FullPersonEntity::email.name),
            bornDate = document.getString(FullPersonEntity::bornDate.name),
            age = document.getInteger(FullPersonEntity::age.name),
            country = mapCountry(document.get(FullPersonEntity::country.name, Document::class.java)),
            interests = mapInterests(document.getList(FullPersonEntity::interests.name, Document::class.java)),
            gender = document.getString(FullPersonEntity::gender.name),
            orientations = document.getList(FullPersonEntity::orientations.name, String::class.java),
            mobilePhoneVerified = document.getBoolean(FullPersonEntity::mobilePhoneVerified.name),
            emailVerified = document.getBoolean(FullPersonEntity::emailVerified.name),
            profileVerified = document.getBoolean(FullPersonEntity::profileVerified.name),
            shortDescription = document.getString(FullPersonEntity::shortDescription.name),
            workPlace = document.getString(FullPersonEntity::workPlace.name),
            whereLive = document.getString(FullPersonEntity::whereLive.name),
            professionalRole = document.getString(FullPersonEntity::professionalRole.name),
            picture = document.getString(FullPersonEntity::picture.name),
            subscribed = document.getBoolean(FullPersonEntity::subscribed.name),
            promoted = document.getBoolean(FullPersonEntity::promoted.name),
            promotedDate = document.getString(FullPersonEntity::promotedDate.name),
            promotedTimeZone = document.getString(FullPersonEntity::promotedTimeZone.name),
            profilePercentage = document.getInteger(FullPersonEntity::profilePercentage.name),
            photos = mapPhotos(document.getList(FullPersonEntity::photos.name, Document::class.java)),
            preferences = mapPreferences(document.get(FullPersonEntity::preferences.name, Document::class.java)),
            latestLocation = mapLocation(document.get(FullPersonEntity::latestLocation.name, Document::class.java)),
        )

    private fun mapCountry(country: Document?): CountryEntity? =
        country?.let {
            CountryEntity(
                code = it.getString(CountryEntity::code.name),
                name = it.getString(CountryEntity::name.name),
                phoneCode = it.getString(CountryEntity::phoneCode.name)
            )
        }

    private fun mapInterests(interests: List<Document>?): List<InterestEntity> =
        interests?.map {
            InterestEntity(
                id = it.getObjectId(FIELD_ID).toString(),
                name = it.getString(InterestEntity::name.name),
                description = it.getString(InterestEntity::description.name)
            )
        } ?: emptyList()

    private fun mapPhotos(photos: List<Document>?): List<PhotoEntity> =
        photos?.map {
            PhotoEntity(
                id = it.getObjectId(FIELD_ID).toString(),
                personId = it.getString(PhotoEntity::personId.name),
                key = it.getString(PhotoEntity::key.name),
                url = it.getString(PhotoEntity::url.name),
                filename = it.getString(PhotoEntity::filename.name),
                main = it.getBoolean(PhotoEntity::main.name)
            )
        } ?: emptyList()

    private fun mapPreferences(preferences: Document): PreferencesEntity =
        PreferencesEntity(
            pushNotifications = preferences.getBoolean(PreferencesEntity::pushNotifications.name),
            alwaysLocation = preferences.getBoolean(PreferencesEntity::alwaysLocation.name),
            facialRecognition = preferences.getBoolean(PreferencesEntity::facialRecognition.name),
            groupDating = preferences.getBoolean(PreferencesEntity::groupDating.name),
            showSexualOrientation = preferences.getBoolean(PreferencesEntity::showSexualOrientation.name),
            global = preferences.getBoolean(PreferencesEntity::global.name),
            minAge = preferences.getInteger(PreferencesEntity::minAge.name),
            maxAge = preferences.getInteger(PreferencesEntity::maxAge.name),
            maxDistance = preferences.getInteger(PreferencesEntity::maxDistance.name),
            confirmSeen = preferences.getBoolean(PreferencesEntity::confirmSeen.name),
            showMe = preferences.getString(PreferencesEntity::showMe.name),
            ulikmeNews = preferences.getBoolean(PreferencesEntity::ulikmeNews.name),
            showProfessionalRole = preferences.getBoolean(PreferencesEntity::showProfessionalRole.name),
            showGender = preferences.getBoolean(PreferencesEntity::showGender.name),
            recentActivity = preferences.getBoolean(PreferencesEntity::recentActivity.name),
            showOnlyInRange = preferences.getBoolean(PreferencesEntity::showOnlyInRange.name),
            showOnlyUntilAge = preferences.getBoolean(PreferencesEntity::showOnlyUntilAge.name),
            showMeInUlikme = preferences.getBoolean(PreferencesEntity::showMeInUlikme.name)
        )

    private fun mapLocation(latestLocation: Document?): LocationEntity? =
        latestLocation?.let {
            LocationEntity(
                id = it.getObjectId(FIELD_ID).toString(),
                coordinates = it.getList(LocationEntity::coordinates.name, Double::class.javaObjectType).toTypedArray(),
                type = it.getString(LocationEntity::type.name)
            )
        }

}