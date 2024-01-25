package dev.payqa.scaffolding.apicrud.design.models

import dev.payqa.scaffolding.apicrud.design.rest.rr.response.Response

data class Page<T : Model>(

    val total: Long,
    val itemsCount: Int,
    val items: List<T>

) : Response() {

    companion object {
        @JvmStatic
        fun <T : Model> of(total: Long, items: List<T>) = Page(total, items.size, items)
    }

}