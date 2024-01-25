package com.ulikme.interaction.api.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "app.config")
open class PropertiesConfiguration(
    var maxDailyUlikme: Int = 0,
    var maxDailyLike: Int = 0
)