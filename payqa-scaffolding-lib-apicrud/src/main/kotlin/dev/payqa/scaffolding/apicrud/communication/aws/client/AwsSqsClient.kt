package dev.payqa.scaffolding.apicrud.communication.aws.client

import dev.payqa.scaffolding.apicrud.design.exceptions.ApiException
import dev.payqa.scaffolding.apicrud.design.exceptions.CommunicationException
import dev.payqa.scaffolding.apicrud.design.exceptions.ExceptionDetail
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.DisposableBean
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.*

class AwsSqsClient(
    region: Region,
    accessKey: String,
    secretKey: String

) : DisposableBean {

    companion object {
        private val log = LoggerFactory.getLogger(AwsSqsClient::class.java)
    }
    val creds: AwsCredentialsProvider = StaticCredentialsProvider.create(
        AwsBasicCredentials.create(accessKey, secretKey)
    )

    private val client: SqsClient = SqsClient.builder().region(region).credentialsProvider(creds).build()

    private fun queueUrl(queueName: String): String =
        client.getQueueUrl(
            GetQueueUrlRequest.builder()
                .queueName(queueName)
                .build()
        ).queueUrl()

    /**
     * Read all enqueued messages into aws
     *
     * @param queueName aws simple queue name
     */
    fun retrieveMessages(queueName: String): List<Message> =
        client.receiveMessage(
            ReceiveMessageRequest.builder()
                .queueUrl(queueUrl(queueName))
                .build()
        ).messages()

    /**
     * Enqueue a simple message to aws-sqs,
     * checking if it has two required attributes:
     * MessageDeduplicationId & MessageGroupId
     *
     * @param queueName aws simple queue name
     * @param message aws message to enqueue
     */
    fun sendMessage(queueName: String, message: Message): SendMessageResponse {
        log.info("[sendMessage] init. [queue=$queueName, message=$message]")

        if (!message.hasMessageAttributes()) {
            throw ApiException(
                detail = ExceptionDetail(
                    message = "Message must have following attributes: " +
                            "${MessageSystemAttributeName.MESSAGE_GROUP_ID}." +
                            "If queue has content-based disables, also must have: " +
                            "${MessageSystemAttributeName.MESSAGE_DEDUPLICATION_ID}"
                )
            )
        }

        return try {
            val messageResponse = client.sendMessage(
                SendMessageRequest.builder()
                    .queueUrl(queueUrl(queueName))
                    .messageDeduplicationId(
                        message.messageAttributes()
                                [MessageSystemAttributeName.MESSAGE_DEDUPLICATION_ID.toString()]?.stringValue()
                    )
                    .messageGroupId(
                        message.messageAttributes()
                                [MessageSystemAttributeName.MESSAGE_GROUP_ID.toString()]!!.stringValue()
                    )
                    .messageBody(message.body())
                    .build()
            )
            log.info("[sendMessage] message send successfully. [response=$messageResponse]")
            messageResponse
        } catch (exception: SqsException) {
            log.error(exception.message, exception)
            throw CommunicationException(queueName, exception.message ?: "", exception)
        }
    }

    /**
     * Delete a specific enqueued message from aws-sqs
     *
     * @param queueName aws simple queue name
     * @param message aws enqueued message to delete
     */
    fun deleteMessage(queueName: String, message: Message): DeleteMessageResponse {
        log.info("[deleteMessage] init. [queue=$queueName, message=$message]")
        val deleteResponse = client.deleteMessage(
            DeleteMessageRequest.builder()
                .queueUrl(queueUrl(queueName))
                .receiptHandle(message.receiptHandle())
                .build()
        )
        log.info("[deleteMessage] message deleted successfully. [response=$deleteResponse]")
        return deleteResponse
    }

    override fun destroy() {
        client.close()
    }

}