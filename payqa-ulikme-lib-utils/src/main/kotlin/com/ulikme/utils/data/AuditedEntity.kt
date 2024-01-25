package com.ulikme.utils.data

import com.ulikme.utils.security.AuthUser
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import java.io.Serializable
import java.time.ZoneId
import java.util.Date

open class AuditedEntity(
    @CreatedBy
    open var createdUser: AuthUser? = null,
    @CreatedDate
    open var createdDate: Date? = null,
    @LastModifiedBy
    open var updatedUser: AuthUser? = null,
    @LastModifiedDate
    open var updatedDate: Date? = null,
    open var timeZone: String = ZoneId.systemDefault().id
) : Serializable
