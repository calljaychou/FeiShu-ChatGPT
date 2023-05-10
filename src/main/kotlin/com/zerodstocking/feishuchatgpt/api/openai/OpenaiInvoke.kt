package com.zerodstocking.feishuchatgpt.api.openai

import com.fasterxml.jackson.core.type.TypeReference
import com.zerodstocking.feishuchatgpt.common.exception.BizException
import com.zerodstocking.feishuchatgpt.common.exception.jsonMapper
import com.zerodstocking.feishuchatgpt.common.logger
import java.net.URI
import java.time.Instant
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * @author JayCHou <a href="calljaychou@qq.com">Email</a>
 * @version 2.0.0
 * @date 2023/4/27 23:58
 */
@Component
class OpenaiInvoke(
    val openaiEnvironment: OpenaiEnvironment,
    val restTemplate: RestTemplate,
    val webClient: WebClient
) {

    fun sendRequest(param: API): Flux<String> {
        val (url, httpEntity) = doRequest(param)
        logger().info("请求openai接口:$url, request: ${jsonMapper().writeValueAsString(httpEntity)}")

        return webClient.post()
            .uri(url)
            .headers { httpEntity.headers }
            .bodyValue(httpEntity.body!!)
            .retrieve().bodyToFlux(String::class.java)
            .onErrorResume(WebClientResponseException::class.java) {
                throw BizException(BizException.SYSTEM_FAILED, "请求openai接口异常， status: ${it.statusCode}")
            }
    }

    fun <T> sendRequest(param: API, responseType: TypeReference<T>): T {
        val l = Instant.now().toEpochMilli()

        val (url, httpEntity) = doRequest(param)
        logger().info("请求openai接口:$url, request: ${jsonMapper().writeValueAsString(httpEntity)}")

        // 设定请求参数
        val resp = restTemplate.exchange(
            url, param.request.method, httpEntity, ByteArray::class.java
        )
        val responseString = resp.body?.toString(Charsets.UTF_8) ?: ""
        logger().info(
            "请求openai接口:$url, request: ${jsonMapper().writeValueAsString(httpEntity)} ,response: $responseString, 耗时:${
                Instant.now().toEpochMilli() - l
            }"
        )
        if (resp.statusCode == HttpStatus.OK) {
            return responseString.let { jsonMapper().readValue(it, responseType) }
        } else {
            throw BizException(
                BizException.SYSTEM_FAILED, "请求openai接口异常， status: ${resp.statusCode}"
            )
        }
    }

    /**
     * 填充请求入参
     * @param [param] 参数
     * @return [Pair<URI, HttpEntity<*>>]
     */
    private fun doRequest(param: API): Pair<URI, HttpEntity<*>> {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers["Authorization"] = "Bearer ${openaiEnvironment.apiKey}"
        headers["OpenAI-Organization"] = openaiEnvironment.organizationId

        val body: HashMap<String, Any> = jsonMapper().readValue(
            jsonMapper().writeValueAsString(param),
            object : TypeReference<HashMap<String, Any>>() {}
        )
        return Pair(URI(openaiEnvironment.baseURL + param.request.url), HttpEntity(body, headers))
    }

}