package dev.payqa.scaffolding.apicrud.communication.aws.client

import dev.payqa.scaffolding.apicrud.design.exceptions.http.InternalServerErrorException
import java.io.File

data class S3Key(
    val objectName: String? = null,
    val parentFolders: List<String>? = null
) {
    constructor(objectId: String?, vararg parentFolders: String) : this(objectId, parentFolders.asList())

    fun build(): String = parentFolders?.joinToString(File.separator)?.plus(File.separator)?.plus(objectName)
        ?: throw InternalServerErrorException("Parent folders or object id doesn't have appropriated values.")
}