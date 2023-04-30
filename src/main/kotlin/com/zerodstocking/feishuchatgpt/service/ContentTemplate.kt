package com.zerodstocking.feishuchatgpt.service

import com.zerodstocking.feishuchatgpt.model.ReceiveEventInfo

/**
 * @author JayCHou <a href="calljaychou@qq.com">Email</a>
 * @version 2.0.0
 * @date 2023/4/30 00:10
 */
interface ContentTemplate {

    fun message(isAtRoBotMessage: Boolean, event: ReceiveEventInfo, openaiContent: String): String
}