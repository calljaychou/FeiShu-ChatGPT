package com.zerodstocking.feishuchatgpt.api.openai

import org.springframework.http.HttpMethod

/**
 * @author JayCHou <a href="calljaychou@qq.com">Email</a>
 * @version 2.0.0
 * @date 2023/4/28 00:06
 */
enum class OpenaiAPIEnum(
    var url: String,
    val method: HttpMethod
) {
    /**
     * 获取openai模型
     */
    GET_MODELS("/v1/models", HttpMethod.GET),

    /**
     * 发送聊天
     */
    CHAT("/v1/chat/completions", HttpMethod.POST),
}