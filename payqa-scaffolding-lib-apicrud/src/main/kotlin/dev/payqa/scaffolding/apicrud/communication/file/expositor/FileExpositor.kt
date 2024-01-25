package dev.payqa.scaffolding.apicrud.communication.file.expositor

import dev.payqa.scaffolding.apicrud.communication.file.connector.FileServerConnector
import dev.payqa.scaffolding.apicrud.data.enums.DataErrorEnum
import dev.payqa.scaffolding.apicrud.data.exceptions.DataException
import dev.payqa.scaffolding.apicrud.design.exceptions.ContentException
import org.apache.commons.io.FilenameUtils
import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Component
import org.springframework.util.Base64Utils
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException

@Component
open class FileExpositor(
    private val fileServerConnector: FileServerConnector
) {

    companion object {
        private val log = LoggerFactory.getLogger(FileExpositor::class.java.simpleName)
    }

    /**
     *
     * @param folder
     * @param filename
     * @return
     */
    fun download(folder: String, filename: String): Resource {
        return try {
            val file: File = fileServerConnector.downloadFile(folder, filename)
            UrlResource(file.toURI())
        } catch (exception: IOException) {
            log.error(exception.message, exception)
            throw ContentException(exception.message!!, exception)
        }
    }

    /**
     *
     * @param folder
     * @param file
     * @param name
     * @return
     */
    fun upload(folder: String, file: MultipartFile, name: String = ""): String {
        // Obtaining extension from file
        val extension = FilenameUtils.getExtension(file.originalFilename)
            ?: throw DataException(DataErrorEnum.INVALID_DATA, "Cannot obtain extension from file")
        // Transforming filename to base-64 format
        val filename =
            if (name.isEmpty()) file.originalFilename else Base64Utils.encodeToString(name.toByteArray()) + "." + extension
        try {
            fileServerConnector.uploadFile(folder, filename!!, file.inputStream)
        } catch (exception: IOException) {
            log.error(exception.message, exception)
            throw DataException(DataErrorEnum.INVALID_DATA, exception.message!!)
        }
        return filename
    }

}