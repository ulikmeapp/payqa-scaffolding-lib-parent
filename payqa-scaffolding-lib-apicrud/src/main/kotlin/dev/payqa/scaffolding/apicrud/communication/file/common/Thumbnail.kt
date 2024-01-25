package dev.payqa.scaffolding.apicrud.communication.file.common

data class Thumbnail(val width: Int, val height: Int, val quality: Double) {

    constructor(size: Int, quality: Double) : this(size, size, quality)

}