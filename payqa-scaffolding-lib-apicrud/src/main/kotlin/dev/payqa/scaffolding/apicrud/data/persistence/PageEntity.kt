package dev.payqa.scaffolding.apicrud.data.persistence

import dev.payqa.scaffolding.apicrud.design.models.Model
import dev.payqa.scaffolding.apicrud.design.models.Page
import dev.payqa.scaffolding.apicrud.design.models.mapper.ModelMapper

open class PageEntity<K>(
    open var items: List<K> = emptyList(),
    open var total: Long = 0,
    open var page: Int = 0
) {

    val pageSize get() = items.size

    fun <T : Model> toModel(mapper: ModelMapper<T, K>): Page<T> =
        Page.of(this.total, mapper.inverseMap(this.items))

}