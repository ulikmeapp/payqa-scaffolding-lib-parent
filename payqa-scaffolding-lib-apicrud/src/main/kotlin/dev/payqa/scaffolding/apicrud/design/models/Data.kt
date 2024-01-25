package dev.payqa.scaffolding.apicrud.design.models

import dev.payqa.scaffolding.apicrud.design.rest.rr.response.Response

class Data<T : Model>(
    val items: List<T> = emptyList()
) : Response() {

    companion object {
        @JvmStatic
        fun <T : Model> of(items: List<T>): Data<T> =
            Data(items)
    }

    var count: Int = this.items.size; private set

    fun isEmpty() = this.items.isEmpty()

    fun isNotEmpty() = this.items.isNotEmpty()

    fun hasOnlyOne() = this.items.size == 1

}