package dev.payqa.scaffolding.apicrud.communication.retrofit.config

object RetrofitConfig {

    var dateAsLong: Boolean = false

    fun mapDateAsLong(): RetrofitConfig {
        dateAsLong = true
        return this
    }

}