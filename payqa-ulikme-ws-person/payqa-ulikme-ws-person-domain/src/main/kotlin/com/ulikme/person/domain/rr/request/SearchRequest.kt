package com.ulikme.person.domain.rr.request

import java.math.BigDecimal

data class SearchRequest(
    /**
     * Latitude
     */
    val lat: BigDecimal? = null,
    /**
     * Longitude
     */
    val lng: BigDecimal? = null,
    /**
     * Distance
     */
    val dst: Int? = null,/**/
    /**
     * Show me (gender)
     */
    val sm: String? = null,
    /**
     * Age
     */
    val age: Int? = null,
    /**
     * Age
     */
    val minAge: Int? = null,
    /**
     * Country
     */
    val ctr: String? = null,
    /**
     * User that is requesting
     */
    val usr: String? = null
) {

    fun toMap(): Map<String, Any> =
        mutableMapOf<String, Any>().apply {
            lat?.let { put(SearchRequest::lat.name, it) }
            lng?.let { put(SearchRequest::lng.name, it) }
            dst?.let { put(SearchRequest::dst.name, it) }
            sm?.let { put(SearchRequest::sm.name, it) }
            age?.let { put(SearchRequest::age.name, it) }
            minAge?.let { put(SearchRequest::minAge.name, it) }
            ctr?.let { put(SearchRequest::ctr.name, it) }
            usr?.let { put(SearchRequest::usr.name, it) }
        }

}