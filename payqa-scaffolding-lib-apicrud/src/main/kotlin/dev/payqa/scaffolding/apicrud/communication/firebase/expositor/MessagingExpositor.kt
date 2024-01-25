package dev.payqa.scaffolding.apicrud.communication.firebase.expositor

import com.google.firebase.messaging.BatchResponse
import com.google.firebase.messaging.MulticastMessage
import com.google.firebase.messaging.Notification
import dev.payqa.scaffolding.apicrud.communication.firebase.connector.FirebaseConnector
import org.springframework.stereotype.Component

@Component
open class MessagingExpositor(
    private val connector: FirebaseConnector
) {

    fun send(
        title: String = "Payqa",
        message: String,
        data: Map<String, String> = emptyMap(),
        registrationTokens: List<String>
    ): BatchResponse = with(MulticastMessage.builder()) {
        setNotification(
            with(Notification.builder()) {
                setTitle(title)
                setBody(message)
                build()
            }
        )
        data.forEach { (key, value) -> putData(key, value) }
        addAllTokens(registrationTokens)
        connector.messaging.sendMulticast(build())
    }

}