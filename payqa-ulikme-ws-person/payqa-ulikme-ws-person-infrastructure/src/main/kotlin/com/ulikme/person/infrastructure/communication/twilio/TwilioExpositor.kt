package com.ulikme.person.infrastructure.communication.twilio

import com.ulikme.person.infrastructure.communication.twilio.enums.MessageType
import com.ulikme.person.infrastructure.communication.twilio.enums.Phone
import com.twilio.Twilio
import com.twilio.rest.api.v2010.account.Message
import com.twilio.rest.api.v2010.account.MessageCreator
import com.twilio.type.PhoneNumber
import dev.payqa.scaffolding.apicrud.design.exceptions.http.BadRequestException
import dev.payqa.scaffolding.apicrud.design.exceptions.http.InternalServerErrorException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
open class TwilioExpositor(
    @Value("\${twilio.account-sid}") accountSid: String,
    @Value("\${twilio.auth-token}") authToken: String,
    @Value("\${twilio.enabled}") private val enabled: Boolean
) {

    init {
        Twilio.init(accountSid, authToken)
    }

    fun sendSMS(message: String, phoneNumber: String) = this.sendMessage(message, phoneNumber, MessageType.SMS)

    fun sendWhatsapp(message: String, phoneNumber: String) =
        this.sendMessage("whatsapp:$message", phoneNumber, MessageType.WHATSAPP)

    private fun sendMessage(content: String, phoneNumber: String, type: MessageType) {
        if (enabled) {
            try {
                Message.creator(
                    PhoneNumber(phoneNumber.let {
                        if (!it.startsWith("+")) "+$it" else it
                    }),
                    PhoneNumber(
                        when (type) {
                            MessageType.SMS -> Phone.SMS.number
                            MessageType.WHATSAPP -> Phone.WHATSAPP.number
                            MessageType.FB_MESSENGER -> throw InternalServerErrorException("Facebook messenger has not been implemented")
                        }
                    ), content
                ).create()
            } catch (exception: Exception) {
                throw BadRequestException("Invalid phone number value: $phoneNumber")
            }
        }
    }

}