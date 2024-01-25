package com.ulikme.config.service

interface ConfigService {

    fun getAppProperties(): Map<String, Any>

}