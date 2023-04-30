package com.zerodstocking.feishuchatgpt.api.openai

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
 * @author JayCHou <a href="calljaychou@qq.com">Email</a>
 * @version 2.0.0
 * @date 2023/4/28 00:12
 */
@Component
class OpenaiEnvironment {

    @Value("\${openai.apiKey}")
    lateinit var apiKey: String

    @Value("\${openai.organizationId}")
    lateinit var organizationId: String

    @Value("\${openai.baseURL}")
    lateinit var baseURL: String

}