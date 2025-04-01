package com.storead.common.web

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider
import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.lambda.LambdaClient
import software.amazon.awssdk.services.lambda.model.InvokeRequest
import software.amazon.awssdk.services.lambda.model.InvokeResponse


data class CrawlingResult(
    val status: Boolean,
    val message: String,
    val data: List<String>,
)

data class LambdaResponse(
    private val invokeResponse: InvokeResponse,
) {
    val status: Boolean
    val message: String
    val data: List<String>

    init {
        val response = invokeResponse.payload().asUtf8String()
        val responseMap = ObjectMapper().readValue(response, CrawlingResult::class.java)

        status = responseMap.status
        message = responseMap.message
        data = responseMap.data
    }
}

@Component
class AwsLambdaHandler {

    private val lambda: LambdaClient = LambdaClient.builder()
        .region(Region.AP_NORTHEAST_2)
        .credentialsProvider(ProfileCredentialsProvider.create())
        .build()

    fun call(functionName: String, payload: String): LambdaResponse {
        // TODO: Lambda 호출시 Logging

        val request = InvokeRequest.builder()
            .functionName(functionName)
            .payload(SdkBytes.fromUtf8String(payload))
            .build()

        val invokeResponse = lambda.invoke(request)
        return LambdaResponse(invokeResponse)
    }
}