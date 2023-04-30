package com.zerodstocking.feishuchatgpt.api.feishu

import com.fasterxml.jackson.core.type.TypeReference
import com.zerodstocking.feishuchatgpt.common.FeiShuEnvironment
import com.zerodstocking.feishuchatgpt.common.exception.BizException
import com.zerodstocking.feishuchatgpt.common.exception.jsonMapper
import com.zerodstocking.feishuchatgpt.common.logger
import java.net.URI
import java.time.Instant
import java.util.concurrent.TimeUnit
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

/**
 * @author JayCHou <a href="calljaychou@qq.com">Email</a>
 * @version 2.0.0
 * @date 2023/4/19 22:37
 */
@Component
class FeiShuInvoke(
    val restTemplate: RestTemplate,
    val environment: FeiShuEnvironment,
    val redisTemplate: StringRedisTemplate
) {

    /**
     * 发送请求
     * @param [param] 参数
     * @param [responseType] 响应类型
     * @return [T]
     */
    fun <T> sendRequest(param: FeiShuApi, responseType: TypeReference<T>): T {
        val l = Instant.now().toEpochMilli()

        val (url, httpEntity) = doRequest(param)
        logger().info("请求飞书接口:$url, request: ${jsonMapper().writeValueAsString(httpEntity)}")
        // 设定请求参数
        val resp: ResponseEntity<ByteArray> = restTemplate.exchange(
            url, param.request.method, httpEntity, ByteArray::class.java
        )
        val responseString = resp.body?.toString(Charsets.UTF_8) ?: ""
        logger().info(
            "请求飞书接口:$url, request: ${jsonMapper().writeValueAsString(httpEntity)} ,response: $responseString, 耗时:${
                Instant.now().toEpochMilli() - l
            }"
        )
        if (resp.statusCode == HttpStatus.OK) {
            return responseString.let { jsonMapper().readValue<T>(it, responseType) }
        } else {
            throw BizException(
                BizException.SYSTEM_FAILED, "请求飞书接口异常， status: ${resp.statusCode}, response: $responseString"
            )
        }
    }


    /**
     * 填充请求入参
     * @param [param] 参数
     * @return [Pair<URI, HttpEntity<*>>]
     */
    private fun doRequest(param: FeiShuApi): Pair<URI, HttpEntity<*>> {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON_UTF8

        param.request.tokenType?.run {
            headers["Authorization"] = "Bearer ${
                when (this) {
                    TokenType.TENANT -> getTenantAccessToken()
                    TokenType.APP -> getAppAccessToken()
                }
            }"
        }

        val body: HashMap<String, Any> = jsonMapper().readValue(
            jsonMapper().writeValueAsString(param),
            object : TypeReference<HashMap<String, Any>>() {}
        )
        return Pair(URI(environment.baseURL + param.request.url), HttpEntity(body, headers))
    }

    private fun getTenantAccessToken() = getAccessTokenCache("TENANT_ACCESS_TOKEN") ?: kotlin.run {
        val valueType = object : TypeReference<TenantTokenResult>() {}
        val ret = sendRequest(TenantTokenRequest(environment.appId, environment.appSecret), valueType)
        return if (ResultCode.SUCCESS.code == ret.code) {
            setAccessTokenCache("TENANT_ACCESS_TOKEN", ret.tenantAccessToken!!, ret.expire!!)
            ret.tenantAccessToken!!
        } else {
            throw BizException(BizException.BUSINESS_FAILED, "获取TenantAccessToken失败, 错误码：${ret.msg}")
        }
    }

    private fun getAppAccessToken() = getAccessTokenCache("APP_ACCESS_TOKEN") ?: kotlin.run {
        val valueType = object : TypeReference<AppTokenResult>() {}
        val ret = sendRequest(AppTokenRequest(environment.appId, environment.appSecret), valueType)
        return if (ResultCode.SUCCESS.code == ret.code) {
            setAccessTokenCache("APP_ACCESS_TOKEN", ret.appAccessToken!!, ret.expire!!)
            ret.appAccessToken!!
        } else {
            throw BizException(BizException.BUSINESS_FAILED, "获取AppAccessToken失败, 错误码：${ret.msg}")
        }
    }

    private fun setAccessTokenCache(key: String, token: String, expire: Long) {
        if (expire <= 0) {
            return
        }
        redisTemplate.opsForValue().set(key, token, expire, TimeUnit.SECONDS)
    }

    private fun getAccessTokenCache(key: String) = redisTemplate.opsForValue().get(key)
}