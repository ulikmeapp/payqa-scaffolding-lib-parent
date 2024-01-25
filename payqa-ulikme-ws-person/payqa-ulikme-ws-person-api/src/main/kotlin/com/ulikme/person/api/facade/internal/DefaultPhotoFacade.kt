package com.ulikme.person.api.facade.internal

import com.ulikme.person.api.facade.PhotoFacade
import com.ulikme.person.domain.models.BLUR_IMAGE_FOLDER
import com.ulikme.person.domain.models.BLUR_IMAGE_NAME
import com.ulikme.person.domain.models.PhotoModel
import com.ulikme.person.domain.rr.request.assembler.PhotoRequestAssembler
import com.ulikme.person.infrastructure.communication.common.enums.Bucket
import com.ulikme.person.service.FullPersonService
import com.ulikme.person.service.PersonService
import com.ulikme.person.service.PhotoService
import com.ulikme.utils.rx.RxBgObserver
import dev.payqa.scaffolding.apicrud.communication.aws.client.AwsS3Client
import dev.payqa.scaffolding.apicrud.communication.aws.client.S3Key
import dev.payqa.scaffolding.apicrud.design.exceptions.http.NotFoundException
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import rx.Observable
import rx.schedulers.Schedulers
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import javax.servlet.http.HttpServletRequest

@Service
open class DefaultPhotoFacade(
    private val awsS3Client: AwsS3Client,
    private val photoService: PhotoService,
    private val personService: PersonService,
    private val fullPersonService: FullPersonService
) : PhotoFacade {

    @Cacheable("photo-by-name", key = "#id + #name")
    override fun findByName(id: String, name: String): ByteArray =
        awsS3Client.getObject(
            Bucket.PHOTOS.key,
            S3Key(name, id)
        ).readBytes()

    override fun register(
        id: String,
        file: MultipartFile,
        request: HttpServletRequest
    ): PhotoModel =
        S3Key(file.originalFilename, id).let { s3Key ->
            awsS3Client.putObject(Bucket.PHOTOS.key, s3Key, file.bytes)
            photoService.register(
                PhotoRequestAssembler.toModel(id, file, request, s3Key)
            ).also { mainPhoto ->
                personService.findById(id).let { person ->
                    personService.save(
                        person.copy(
                            complementary = person.complementary.copy(
                                picture = mainPhoto.url
                            )
                        )
                    ).also { fullPersonService.addPhoto(it.id, mainPhoto) }
                }
            }
        }.also {
            with(Schedulers.newThread()) {
                Observable.fromCallable {
                    awsS3Client.getObject(Bucket.PHOTOS.key, S3Key(BLUR_IMAGE_NAME, BLUR_IMAGE_FOLDER))
                        .let { blurImage ->
                            S3Key(BLUR_IMAGE_NAME, id).let { s3Key ->
                                awsS3Client.putObject(Bucket.PHOTOS.key, s3Key, blurImage.readBytes())
                            }
                        }
                }.observeOn(this).subscribeOn(this).subscribe(RxBgObserver("uploadBlurImage"))
            }
        }

    @CacheEvict("photo-by-name", key = "#id + #name")
    override fun delete(id: String, name: String) {
        photoService.findByPersonAndNameOrNull(id, name)?.let { photoToDelete ->
            photoService.delete(photoToDelete.id)
            fullPersonService.removePhoto(id, photoToDelete.id)
        } ?: throw NotFoundException("Cannot find a photo with name: $name")
    }

    private fun resize(multipartFile: MultipartFile): ByteArray =
        ImageIO.read(ByteArrayInputStream(multipartFile.bytes)).let { bufferedImage ->
            BufferedImage(500, 1000, BufferedImage.TYPE_INT_RGB).apply {
                graphics.drawImage(bufferedImage.getScaledInstance(500, 1000, Image.SCALE_DEFAULT), 0, 0, null)
            }.let { resizedImage ->
                ByteArrayOutputStream().apply { ImageIO.write(resizedImage, "jpeg", this) }.toByteArray()
            }
        }

}