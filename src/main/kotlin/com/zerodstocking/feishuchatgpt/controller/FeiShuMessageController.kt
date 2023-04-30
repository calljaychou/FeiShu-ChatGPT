package com.zerodstocking.feishuchatgpt.controller

import com.zerodstocking.feishuchatgpt.common.annotation.FeiShuMessageRequest
import com.zerodstocking.feishuchatgpt.common.responseSuccess
import com.zerodstocking.feishuchatgpt.mq.FeiShuMessageProducer
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


/**
 * @author JayCHou <a href="calljaychou@qq.com">Email</a>
 * @version 2.0.0
 * @date 2023/4/22 23:29
 */
@RestController
@RequestMapping("/v1/webhook/event")
class FeiShuMessageController(val feiShuMessageProducer: FeiShuMessageProducer) {


    /**
     * 接收并处理事件,文档参考:
     * https://open.feishu.cn/document/ukTMukTMukTM/uYDNxYjL2QTM24iN0EjN/event-subscription-configure-/encrypt-key-encryption-configuration-case
     */
    @PostMapping
    fun event(@FeiShuMessageRequest request: String) = run {
        feiShuMessageProducer.production(request)
        responseSuccess()
    }

}