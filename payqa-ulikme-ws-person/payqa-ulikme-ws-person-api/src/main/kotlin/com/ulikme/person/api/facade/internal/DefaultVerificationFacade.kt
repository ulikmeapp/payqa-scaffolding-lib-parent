package com.ulikme.person.api.facade.internal

import com.ulikme.person.api.facade.PhotoFacade
import com.ulikme.person.api.facade.VerificationFacade
import com.ulikme.person.domain.enums.VerificationType
import com.ulikme.person.domain.models.PhotoModel
import com.ulikme.person.domain.rr.request.assembler.PhotoRequestAssembler
import com.ulikme.person.domain.rr.response.VerifyResponse
import com.ulikme.person.infrastructure.communication.common.enums.Bucket
import com.ulikme.person.infrastructure.communication.twilio.TwilioExpositor
import com.ulikme.person.infrastructure.persistence.mongo.entities.CodeVerificationEntity
import com.ulikme.person.infrastructure.persistence.mongo.repositories.CodeVerificationRepository
import com.ulikme.person.infrastructure.persistence.mongo.repositories.PhotoVerificationRepository
import com.ulikme.person.service.CodeVerificationService
import com.ulikme.person.service.PersonService
import com.ulikme.person.service.PhotoService
import dev.payqa.scaffolding.apicrud.communication.aws.client.AwsS3Client
import dev.payqa.scaffolding.apicrud.communication.aws.client.S3Key
import dev.payqa.scaffolding.apicrud.data.utils.Validator
import dev.payqa.scaffolding.apicrud.design.exceptions.http.InternalServerErrorException
import dev.payqa.scaffolding.apicrud.design.exceptions.http.NotFoundException
import dev.payqa.scaffolding.utils.common.CodeUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import javax.servlet.http.HttpServletRequest

@Service
open class DefaultVerificationFacade(
    private val awsS3Client: AwsS3Client,
    private val twilioExpositor: TwilioExpositor,
    private val personService: PersonService,
    private val photoService: PhotoService,
    private val codeVerificationService: CodeVerificationService
) : VerificationFacade {

    @Transactional
    override fun verifyData(personId: String, mobilePhone: String?): VerifyResponse {
        Validator.checkNotNull(mobilePhone, "mobilePhone")
        Validator.checkNotEmpty(mobilePhone, "mobilePhone")
        return personService.findById(personId).let { person ->
            mobilePhone!!.let { mobilePhone ->
                // Register a new verification code
                val verificationCode = codeVerificationService.register(
                    person.id,
                    VerificationType.MOBILE_PHONE.value,
                    CodeUtils.generateCode().let { if (it.length > 6) it.substring(0, 6) else it }
                )
                // Send SMS by Twilio
                /*twilioExpositor.sendSMS(
                    "You have requested a mobile phone verification. " +
                            "Please put this code <$verificationCode> to verify it.",
                    mobilePhone
                )*/
                // Update user in another thread
                Executors.newCachedThreadPool().submit(
                    Callable {
                        personService.save(
                            person.copy(
                                personal = person.personal.copy(
                                    mobilePhone = mobilePhone
                                )
                            )
                        )
                    }
                )
                // Build verify response
                VerifyResponse(
                    success = true,
                    message = "Message send to $mobilePhone"
                )
            }
        }
    }

    override fun verifyProfile(personId: String, photo: MultipartFile, request: HttpServletRequest): PhotoModel =
        S3Key(photo.originalFilename, personId).let { s3Key ->
            awsS3Client.putObject(Bucket.PHOTOS.key, s3Key, photo.bytes)
            photoService.register(
                PhotoRequestAssembler.toModel(personId, photo, request, s3Key),
                true
            )
        }

    @Transactional
    override fun validateData(personId: String, verificationCode: String) {
        personService.findById(personId).let { person ->
            codeVerificationService.findLatestByPersonId(personId)
                .let { savedVerificationCode ->
                    if (savedVerificationCode != verificationCode) {
                        throw NotFoundException("Verification code has not been found.")
                    } else {
                        Executors.newCachedThreadPool().submit(
                            Callable {
                                personService.save(
                                    person.copy(
                                        behavior = person.behavior.copy(
                                            mobilePhoneVerified = true
                                        )
                                    )
                                )
                            }
                        )
                    }
                }
        }

    }

    override fun validateProfile(personId: String, valid: Boolean) {
        when(valid) {
            true ->
                personService.findById(personId).let { person ->
                    personService.save(
                        person.copy(
                            behavior = person.behavior.copy(
                                profileVerified = true
                            )
                        )
                    )
                }
            false -> Unit // TODO: Implement call to repository to persist no valid verification
        }
    }

}