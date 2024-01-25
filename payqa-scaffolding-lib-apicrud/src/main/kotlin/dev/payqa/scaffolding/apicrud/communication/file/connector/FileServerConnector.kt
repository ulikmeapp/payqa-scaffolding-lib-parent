package dev.payqa.scaffolding.apicrud.communication.file.connector

import dev.payqa.scaffolding.apicrud.data.exceptions.ValidationException
import dev.payqa.scaffolding.apicrud.design.exceptions.ContentException
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory
import java.io.*

class FileServerConnector(
    val fileServerPath: String
) {

    companion object {
        private val log = LoggerFactory.getLogger(FileServerConnector::class.java.simpleName)
    }

    /**
     * @param folder
     * @param filename
     * @return
     */
    fun downloadFile(folder: String, filename: String): File {
        val file = FileUtils.getFile(fileServerDirectory(folder, false), filename)
        if (file.exists()) return if (!file.isDirectory) file else throw ContentException("File $filename is a directory, please specify a valid file")
        throw ContentException(String.format("File %s does not exists", filename))
    }

    /**
     * @param folder
     * @param filename
     * @return
     */
    fun downloadFileAsStream(folder: String, filename: String): FileInputStream {
        val file = downloadFile(folder, filename)
        return try {
            FileInputStream(file)
        } catch (exception: FileNotFoundException) {
            log.error(exception.message, exception)
            throw ContentException(String.format("Invalid caught of stream from file %s, it not exists", filename))
        }
    }

    /**
     * @param folder
     * @param filename
     * @param stream
     */
    @Synchronized
    fun uploadFile(folder: String, filename: String, stream: InputStream?) {
        val fileDirectory = fileServerDirectory(folder, true)
        val file = File(fileDirectory.absolutePath + File.separator + filename)
        try {
            FileUtils.writeByteArrayToFile(file, IOUtils.toByteArray(stream))
        } catch (exception: IOException) {
            log.error(exception.message, exception)
            throw ContentException("File cannot be uploaded", exception)
        }
    }

    private fun fileServerDirectory(additional: String, createDirectory: Boolean): File {
        ValidationException.checkNotNull(additional, "Directory to manage files from file server cannot be null")
        ValidationException.checkTrue(
            additional.isNotEmpty(),
            "Directory to manage files from file server cannot be empty"
        )
        val directory = File(fileServerPath + File.separator + additional)
        if (directory.exists()) return directory else if (createDirectory) directory.mkdir()
        return directory
    }

}