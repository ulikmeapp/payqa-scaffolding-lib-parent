package com.ulikme.utils.data.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

const val FIELD_ID = "_id"

@Configuration
open class MongoConfiguration(
    @Value("\${spring.data.mongodb.database}") val database: String
)