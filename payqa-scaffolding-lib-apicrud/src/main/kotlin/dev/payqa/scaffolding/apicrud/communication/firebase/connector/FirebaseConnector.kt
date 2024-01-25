package dev.payqa.scaffolding.apicrud.communication.firebase.connector

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import java.io.FileInputStream

class FirebaseConnector(
    serviceKeyPath: String
) {

    private val app: FirebaseApp = FirebaseApp.initializeApp(
        FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(FileInputStream(serviceKeyPath)))
            .build()
    )

    val messaging: FirebaseMessaging = FirebaseMessaging.getInstance(this.app)

}