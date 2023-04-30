package com.zerodstocking.feishuchatgpt.service

import com.zerodstocking.feishuchatgpt.common.exception.jsonMapper
import com.zerodstocking.feishuchatgpt.model.ReceiveEventInfo
import org.springframework.stereotype.Service

/**
 * @author JayCHou <a href="calljaychou@qq.com">Email</a>
 * @version 2.0.0
 * @date 2023/4/30 00:11
 */
@Service
class CardContentTemplate : ContentTemplate {

    override fun message(isAtRoBotMessage: Boolean, event: ReceiveEventInfo, replyContent: String): String {
        val at =
            if (isAtRoBotMessage) "<at user_id=\"${event.eventSender!!.senderId!!.openId!!}\"></at>"
            else ""

        val config = hashMapOf(
            "wide_screen_mode" to true,
            "update_multi" to true
        )

        val elements = arrayListOf(
            hashMapOf(
                "tag" to "div",
                "text" to hashMapOf("tag" to "lark_md", "content" to at)
            ),
            hashMapOf(
                "tag" to "div",
                "text" to hashMapOf("tag" to "lark_md", "content" to replyContent)
            )
        )

        return jsonMapper().writeValueAsString(
            hashMapOf(
                "config" to config,
                "elements" to elements
            )
        )
    }
}