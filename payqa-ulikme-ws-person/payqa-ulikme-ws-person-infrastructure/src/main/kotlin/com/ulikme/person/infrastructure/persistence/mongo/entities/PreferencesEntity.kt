package com.ulikme.person.infrastructure.persistence.mongo.entities

import com.ulikme.utils.data.AuditedEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable

@Document(collection = "preferencesV2")
open class PreferencesEntity(
    @Id
    open var id: String? = null,
    open var personId: String = "",
    open var pushNotifications: Boolean = false,
    open var alwaysLocation: Boolean = false,
    open var facialRecognition: Boolean = false,
    open var groupDating: Boolean = false,
    open var showSexualOrientation: Boolean = false,
    open var global: Boolean = false,
    open var minAge: Int? = null,
    open var maxAge: Int? = null,
    open var maxDistance: Int? = null,
    open var confirmSeen: Boolean = false,
    open var showMe: String? = null,
    open var ulikmeNews: Boolean = false,
    open var showProfessionalRole: Boolean = false,
    open var showGender: Boolean = false,
    open var recentActivity: Boolean = false,
    open var showOnlyInRange: Boolean = false,
    open var showOnlyUntilAge: Boolean = false,
    open var showMeInUlikme: Boolean = false,
    open var firebaseTokens: List<String> = emptyList()
) : AuditedEntity(), Serializable