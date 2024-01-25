package com.ulikme.person.infrastructure.persistence.mongo.repositories

import com.ulikme.person.infrastructure.persistence.mongo.entities.FullPersonEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface FullPersonMongoRepository {

    /**
     * Main application search (retrieves a list of person who consider the specified key parameters
     * by the user that is requesting the main search)
     *
     * @param params
     *
     * usr -> user that is requesting the main search (required)
     *
     * lat -> latitude from where usr is requesting the main search
     *
     * lng -> longitude from where usr is requesting the main search
     *
     * dst -> distance in miles specified by usr to consider into the main search
     *
     * sm -> gender preferred by usr to consider into the main search
     *
     * age -> age preferred by usr to consider into the main search
     * @param pageable params to resolve results as pagination
     *
     */
    fun findAll(params: Map<String, Any>, pageable: Pageable): Page<FullPersonEntity>

    /**
     * Search only person with specified ids (considering the specified key parameters
     * by the user that is requesting the search)
     *
     * @param params
     *
     * usr -> user that is requesting the main search (required)
     *
     * lat -> latitude from where usr is requesting the main search
     *
     * lng -> longitude from where usr is requesting the main search
     *
     * dst -> distance in miles specified by usr to consider into the main search
     *
     * sm -> gender preferred by usr to consider into the main search
     *
     * age -> age preferred by usr to consider into the main search
     * @param includedIds ids to search according to key parameters
     */
    fun findOnly(params: Map<String, Any>, vararg includedIds: String): List<FullPersonEntity>

}