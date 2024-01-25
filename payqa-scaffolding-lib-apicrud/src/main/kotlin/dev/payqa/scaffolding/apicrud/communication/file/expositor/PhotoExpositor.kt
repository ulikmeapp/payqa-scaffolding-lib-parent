package dev.payqa.scaffolding.apicrud.communication.file.expositor

import dev.payqa.scaffolding.apicrud.communication.file.common.Thumbnail
import dev.payqa.scaffolding.apicrud.communication.file.connector.FileServerConnector
import dev.payqa.scaffolding.apicrud.data.enums.DataErrorEnum
import dev.payqa.scaffolding.apicrud.data.exceptions.DataException
import net.coobird.thumbnailator.Thumbnails
import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Component
import org.springframework.util.Base64Utils
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException

@Component
open class PhotoExpositor(
    private val fileServerConnector: FileServerConnector
) {

    companion object {
        private val log = LoggerFactory.getLogger(PhotoExpositor::class.java.simpleName)
    }

    /**
     * Transform a downloaded file to image in base-64 format
     *
     * @param folder folder the contains file to download
     * @param filename name of file that will be downloaded
     * @param thumbnail thumbnail to download, for no download original image
     * @return image in base-64 format
     */
    fun toBase64(folder: String, filename: String, thumbnail: Thumbnail? = null): String =
        try {
            val filenameWithoutExtension = filename.substring(0, filename.indexOf("."))
            fileServerConnector.downloadFileAsStream(
                folder,
                filenameWithoutExtension + (if (thumbnail != null) "_${thumbnail.width}x${thumbnail.height}" else "") + ".png"
            ).use { stream ->
                val bytes = ByteArray(stream.available())
                stream.read(bytes, 0, bytes.size)
                Base64Utils.encodeToString(bytes)
            }
        } catch (exception: IOException) {
            log.error(exception.message, exception)
            throw DataException(DataErrorEnum.INVALID_DATA, exception.message!!)
        }

    fun toStream(folder: String, filename: String, thumbnail: Thumbnail? = null): FileInputStream =
        try {
            val filenameWithoutExtension = filename.substring(0, filename.indexOf("."))
            fileServerConnector.downloadFileAsStream(
                folder,
                filenameWithoutExtension + (if (thumbnail != null) "_${thumbnail.width}x${thumbnail.height}" else "") + ".png"
            )
        } catch (exception: IOException) {
            log.error(exception.message, exception)
            throw DataException(DataErrorEnum.INVALID_DATA, exception.message!!)
        }

    /**
     * Transform a downloaded file to resource within image
     *
     * @param folder folder the contains file to download
     * @param filename name of file that will be downloaded
     * @param thumbnail thumbnail to download, for no download original image
     * @return resource within image
     */
    fun toResource(folder: String, filename: String, thumbnail: Thumbnail? = null): Resource =
        try {
            val filenameWithoutExtension = filename.substring(0, filename.indexOf("."))
            fileServerConnector.downloadFile(
                folder,
                filenameWithoutExtension + (if (thumbnail != null) "_${thumbnail.width}x${thumbnail.height}" else "") + ".png"
            ).let { file -> UrlResource(file.toURI()) }
        } catch (exception: IOException) {
            log.error(exception.message, exception)
            throw DataException(DataErrorEnum.INVALID_DATA, exception.message!!)
        }

    /**
     * Upload a photo, with name in base-64 format
     *
     * @param folder folder's name where image will be
     * @param base64Image image in base-64 format to upload
     * @param name name that image has into folder container
     * @param thumbnails thumbnails to upload with specific characteristics
     * @return name of the image in base-64 format
     */
    fun fromBase64(folder: String, base64Image: String, name: String, vararg thumbnails: Thumbnail): String =
        this.uploadPhoto(
            folder,
            Base64Utils.decodeFromString(
                when (base64Image.indexOf(',') != -1) {
                    true -> base64Image.substring(base64Image.indexOf(',') + 1)
                    false -> base64Image
                }
            ),
            name, thumbnails.toList()
        )

    /**
     * Upload a photo as file, with name in base-64 format
     *
     * @param folder folder's name where image will be
     * @param fileImage image into file
     * @param name name that image has into folder container
     * @param thumbnails thumbnails to upload with specific characteristics
     * @return name of the image in base-64 format
     */
    fun fromFile(folder: String, fileImage: MultipartFile, name: String, vararg thumbnails: Thumbnail): String =
        this.uploadPhoto(folder, fileImage.bytes, name, thumbnails.toList())

    private fun uploadPhoto(
        folder: String,
        bytesImage: ByteArray,
        name: String,
        thumbnails: List<Thumbnail>
    ): String {
        val base64Name = Base64Utils.encodeToString(name.toByteArray())
        val filename = "$base64Name.png"
        try {
            ByteArrayInputStream(bytesImage).use { byteStream ->
                fileServerConnector.uploadFile(folder, filename, byteStream)
                // Uploading image as thumbnail
                val file = fileServerConnector.downloadFile(folder, filename)
                this.generateThumbnails(folder, file, base64Name, thumbnails)
            }
        } catch (exception: IOException) {
            log.error(exception.message, exception)
            throw DataException(DataErrorEnum.INVALID_DATA, exception.message!!)
        }
        return filename
    }

    private fun generateThumbnails(folder: String, image: File, filename: String, thumbnails: List<Thumbnail>) =
        thumbnails.forEach {
            val thumbnailFilename = this.fileServerConnector.fileServerPath +
                    File.separator +
                    folder +
                    File.separator +
                    "${filename}_${it.width}x${it.height}.png"
            Thumbnails.of(image)
                .size(it.width, it.height)
                .toFile(thumbnailFilename)
        }

}