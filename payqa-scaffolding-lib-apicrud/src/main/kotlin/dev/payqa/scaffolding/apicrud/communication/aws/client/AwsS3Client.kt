package dev.payqa.scaffolding.apicrud.communication.aws.client

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.DisposableBean
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.CreateBucketRequest
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.HeadBucketRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectResponse
import java.io.InputStream


class AwsS3Client(
    region: Region,
    accessKey: String,
    secretKey: String
) : DisposableBean {

    companion object {
        private val log = LoggerFactory.getLogger(AwsS3Client::class.java)
    }
    val creds: AwsCredentialsProvider = StaticCredentialsProvider.create(
        AwsBasicCredentials.create(accessKey, secretKey)
    )
    private val client: S3Client = S3Client.builder().credentialsProvider(creds).region(region).build()

    fun getObject(bucketName: String, key: S3Key): InputStream =
        client.getObject(
            GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key.build())
                .build()
        )

    fun putObject(bucketName: String, key: S3Key, bytes: ByteArray): PutObjectResponse {
        this.createBucketIfNotExists(bucketName)
        return client.putObject(
            PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key.build())
                .build(),
            RequestBody.fromBytes(bytes)
        )
    }

    fun deleteObject(bucketName: String, key: S3Key) {
        client.deleteObject(
            DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key.build())
                .build()
        )
    }

    private fun createBucketIfNotExists(bucketName: String): Unit =
        if (!this.existsBucket(bucketName)) {
            val waiter = client.waiter()
            client.createBucket(
                CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build()
            )
            waiter.waitUntilBucketExists(
                HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build()
            ).matched().response().ifPresent { response ->
                log.debug("[createBucketIfNotExists] bucket created successfully. [response=$response]")
            }
        } else Unit

    private fun existsBucket(bucketName: String): Boolean =
        client.listBuckets().buckets().any { bucket -> bucket.name() == bucketName }

    override fun destroy() = client.close()
}