//package com.ulikme.person.infrastructure
//
//import org.junit.jupiter.api.Test
//import software.amazon.awssdk.core.sync.RequestBody
//import software.amazon.awssdk.regions.Region
//import software.amazon.awssdk.services.s3.S3Client
//import software.amazon.awssdk.services.s3.model.GetObjectRequest
//import software.amazon.awssdk.services.s3.model.ListBucketsRequest
//import software.amazon.awssdk.services.s3.model.ListBucketsResponse
//import software.amazon.awssdk.services.s3.model.PutObjectRequest
//import java.io.IOException
//import java.nio.ByteBuffer
//import java.util.*
//
//
//class S3ReaderTest {
//
//    @Test
//    fun read() {
//        val region: Region = Region.US_EAST_1
//        val s3: S3Client = S3Client.builder()
//            .region(region)
//            .build()
//
//        val listBucketsRequest: ListBucketsRequest = ListBucketsRequest.builder().build()
//        val listBucketsResponse: ListBucketsResponse = s3.listBuckets(listBucketsRequest)
//        listBucketsResponse.buckets().stream().forEach { x -> System.out.println(x.name()) }
//
//        val getObjectRequest = GetObjectRequest.builder()
//            .bucket("ulikme-bucket-01")
//            .key("117821699729374165605/sdasdasd.jpeg")
//            .build()
//
//        val objectRequest = PutObjectRequest.builder()
//            .bucket("ulikme-bucket-01")
//            .key("117821699729374165605/xxx")
//            .build()
//
//        s3.putObject(objectRequest, RequestBody.fromByteBuffer(getRandomByteBuffer(10000)))
//
//         //val x = s3.getObject(getObjectRequest)
//        // println(x)
//    }
//
//    @Throws(IOException::class)
//    private fun getRandomByteBuffer(size: Int): ByteBuffer? {
//        val b = ByteArray(size)
//        Random().nextBytes(b)
//        return ByteBuffer.wrap(b)
//    }
//
//}