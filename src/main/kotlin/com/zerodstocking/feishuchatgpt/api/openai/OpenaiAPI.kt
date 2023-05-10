package com.zerodstocking.feishuchatgpt.api.openai

import com.fasterxml.jackson.core.type.TypeReference
import org.springframework.stereotype.Service

/**
 * @author JayCHou <a href="calljaychou@qq.com">Email</a>
 * @version 2.0.0
 * @date 2023/4/28 00:11
 */
@Service
class OpenaiAPI(private val openai: OpenaiInvoke) {

    /**
     * 聊天
     * @param [content] 内容
     * @return [ChatResponse]
     */
    fun chat(content: String) = openai.sendRequest(
        ChatRequest(
            messages = arrayListOf(ChatRequest.MessageInfo().apply {
                this.content = content
            })
        ),
        object : TypeReference<ChatResponse>() {}
    )

    fun chatStream(content: String) = openai.sendRequest(ChatRequest(
        messages = arrayListOf(ChatRequest.MessageInfo().apply {
            this.content = content
        }),
        stream = true
    ))

}