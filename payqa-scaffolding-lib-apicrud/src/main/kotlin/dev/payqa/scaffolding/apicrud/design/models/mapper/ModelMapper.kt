package dev.payqa.scaffolding.apicrud.design.models.mapper

import dev.payqa.scaffolding.apicrud.design.models.Model

abstract class ModelMapper<K : Model, T> {

    abstract fun map(input: K): T

    abstract fun inverseMap(input: T): K

    open fun map(input: List<K>): List<T> = input.map { map(it) }

    open fun inverseMap(input: List<T>): List<K> = input.map { inverseMap(it) }

}