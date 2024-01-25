package dev.payqa.scaffolding.apicrud.design.rest.rr.request

import dev.payqa.scaffolding.apicrud.data.exceptions.ValidationException
import dev.payqa.scaffolding.apicrud.design.models.Model
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.util.ObjectUtils

@ApiModel
data class PaginateRequest<T : Model>(

    @ApiModelProperty(required = true)
    var page: Int? = 0,

    @ApiModelProperty(required = true)
    var pageSize: Int? = 5,

    @ApiModelProperty(required = false)
    var filter: String? = null,

    @ApiModelProperty(required = false)
    var filterColumn: String? = null,

    @ApiModelProperty(required = false)
    var sortColumn: String? = null,

    @ApiModelProperty(allowableValues = "asc,desc", required = false)
    var sortDirection: String? = null,

    @ApiModelProperty(required = false)
    var params: Map<String, Any> = emptyMap()

) {

    private fun isValidColumn(clazz: Class<T>): Boolean =
        clazz.declaredFields.firstOrNull { it.name.equals(this.sortColumn, true) } == null

    private fun checkSortColumn(clazz: Class<T>) =
        when {
            !ObjectUtils.isEmpty(this.sortColumn) && this.isValidColumn(clazz) ->
                throw ValidationException("Conflict with [sortColumn] ${this.sortColumn} from ${clazz.name}.class")
            else -> Unit
        }

    private fun checkSortDirection() =
        when {
            !this.sortDirection.equals(Sort.Direction.ASC.name, true) &&
                    !this.sortDirection.equals(Sort.Direction.DESC.name, true) ->
                throw ValidationException("Conflict with [sortOrder] ${this.sortDirection}")
            else -> Unit
        }

    private fun isSorted(): Boolean = !ObjectUtils.isEmpty(this.sortColumn) && !ObjectUtils.isEmpty(this.sortDirection)

    fun forRepository(clazz: Class<T>): PageRequest =
        if (this.isSorted()) {
            this.checkSortDirection()
            this.checkSortColumn(clazz)
            PageRequest.of(
                this.page!!, this.pageSize!!,
                Sort.by(
                    Sort.Direction.valueOf(
                        this.sortDirection!!.toUpperCase()
                    ), this.sortColumn
                )
            )
        } else PageRequest.of(this.page!!, this.pageSize!!)

    override fun toString(): String {
        return "PaginateRequest(" +
                "page=$page, " +
                "pageSize=$pageSize, " +
                "filter=$filter, " +
                "filterColumn=$filterColumn, " +
                "sortColumn=$sortColumn, " +
                "sortDirection=$sortDirection, " +
                "params=$params" +
                ")"
    }


}