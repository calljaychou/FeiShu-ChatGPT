package com.zerodstocking.feishuchatgpt.api.feishu

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.zerodstocking.feishuchatgpt.common.annotation.NoArg

/**
 * @author JayCHou <a href="calljaychou@qq.com">Email</a>
 * @version 2.0.0
 * @date 2023/4/19 22:07
 */
sealed class FeiShuApi(
    @JsonIgnore val request: FeiShuAPIEnum
)

@NoArg
data class TenantTokenRequest(
    @field:JsonProperty("app_id")
    var appId: String? = null,
    @field:JsonProperty("app_secret")
    var appSecret: String? = null
) : FeiShuApi(FeiShuAPIEnum.TENANT_ACCESS_TOKEN)


@NoArg
data class AppTokenRequest(
    @field:JsonProperty("app_id")
    var appId: String? = null,
    @field:JsonProperty("app_secret")
    var appSecret: String? = null
) : FeiShuApi(FeiShuAPIEnum.APP_ACCESS_TOKEN)

@NoArg
data class TenantTokenResult(
    @JsonProperty("tenant_access_token")
    var tenantAccessToken: String? = null,
    var expire: Long? = null,
) : Result()

@NoArg
data class AppTokenResult(
    @JsonProperty("app_access_token")
    var appAccessToken: String? = null,
    var expire: Long? = null,
) : Result()

@NoArg
open class Result(
    var code: Int? = null,
    var msg: String? = null,
)

enum class ResultCode(val code: Int) {
    SUCCESS(0)
}