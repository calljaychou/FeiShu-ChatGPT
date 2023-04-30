package com.zerodstocking.feishuchatgpt.common

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
 * @author JayCHou <a href="calljaychou@qq.com">Email</a>
 * @version 2.0.0
 * @date 2023/4/19 21:54
 */
@Component
class FeiShuEnvironment {

    @Value("\${feishu.appId}")
    lateinit var appId: String

    @Value("\${feishu.appSecret}")
    lateinit var appSecret: String

    @Value("\${feishu.baseURL}")
    lateinit var baseURL: String

    @Value("\${feishu.encryptKey}")
    lateinit var encryptKey: String

    @Value("\${feishu.verificationToken}")
    lateinit var verificationToken: String

}