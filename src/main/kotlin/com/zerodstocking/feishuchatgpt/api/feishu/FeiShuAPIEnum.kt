package com.zerodstocking.feishuchatgpt.api.feishu

import org.springframework.http.HttpMethod

/**
 * @author JayCHou <a href="calljaychou@qq.com">Email</a>
 * @version 2.0.0
 * @date 2023/4/19 22:08
 */
enum class FeiShuAPIEnum(
    val url: String,
    val tokenType: TokenType?,
    val method: HttpMethod
) {
    /**
     * 自建应用获取 tenant_access_token
     */
    TENANT_ACCESS_TOKEN("/open-apis/auth/v3/tenant_access_token/internal", null, HttpMethod.POST),

    /**
     * 自建应用获取 app_access_token
     */
    APP_ACCESS_TOKEN("/open-apis/auth/v3/app_access_token/internal", null, HttpMethod.POST),

    /**
     * 获取机器人信息
     */
    GET_ROBOT_INFO("/open-apis/bot/v3/info", TokenType.TENANT, HttpMethod.GET),

    /**
     * 发送消息
     */
    SEND_MESSAGES("/open-apis/im/v1/messages?receive_id_type=$1", TokenType.TENANT, HttpMethod.POST),

    /**
     * 回复消息
     */
    REPLY_MESSAGES("/open-apis/im/v1/messages/$1/reply", TokenType.TENANT, HttpMethod.POST),

    /**
     * 更新消息
     */
    UPDATE_CARD_MESSAGES("/open-apis/im/v1/messages/$1", TokenType.TENANT, HttpMethod.PATCH),
}