package com.zerodstocking.feishuchatgpt.service


import com.fasterxml.jackson.core.type.TypeReference
import com.zerodstocking.feishuchatgpt.api.feishu.ReplyMessagesRequest
import com.zerodstocking.feishuchatgpt.api.feishu.SendMessagesRequest
import com.zerodstocking.feishuchatgpt.api.feishu.robot.RobotAPI
import com.zerodstocking.feishuchatgpt.api.openai.OpenaiAPI
import com.zerodstocking.feishuchatgpt.common.constant.ChatType
import com.zerodstocking.feishuchatgpt.common.constant.MsgType
import com.zerodstocking.feishuchatgpt.common.exception.jsonMapper
import com.zerodstocking.feishuchatgpt.model.FeiShuWebhookRequest
import com.zerodstocking.feishuchatgpt.model.ReceiveEventInfo
import io.netty.channel.ConnectTimeoutException
import java.net.SocketTimeoutException
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils

/**
 * @author JayCHou <a href="calljaychou@qq.com">Email</a>
 * @version 2.0.0
 * @date 2023/4/25 21:50
 */
@Service
class ReceiveEventService(
    private val robot: RobotAPI,
    private val openai: OpenaiAPI,
    private val cardContentTemplate: CardContentTemplate
) : EventService {

    override fun handler(body: String) {
        val valueType = object : TypeReference<FeiShuWebhookRequest<ReceiveEventInfo>>() {}
        val info = jsonMapper().readValue<FeiShuWebhookRequest<ReceiveEventInfo>>(body, valueType)
        val event = info.event!!
        val robotOpenId = "ou_b7c949d50f8a7ad02b62a0c47e729683"

        val isMentionMessage = event.eventMessage!!.mentions?.isNotEmpty() == true
        val isAtRoBotMessage = event.eventMessage!!.mentions?.any { it.id?.openId == robotOpenId } ?: false
        val chatBeforeFilter = chatBeforeFilter(event)

        // 群聊,不处理未at机器人的消息 TODO 后续消息直接过滤
        when (event.eventMessage!!.chatType) {
            ChatType.GROUP.value -> {
                if (!isAtRoBotMessage) {
                    return
                }
                // 群聊使用回复消息，并加上at发送人
                var replyContent = chatBeforeFilter.second
                if (chatBeforeFilter.first) {
                    // 请求GPT
                    replyContent = "我是GPT回复"
                    //requestChat(event.eventMessage!!.content!!)
                }
                robotReplyMessage(info, replyContent, true, MsgType.CARD)
            }

            ChatType.P2P.value -> {
                // 单聊at自己添加提示语
                if (isMentionMessage && !isAtRoBotMessage) {
                    robotReplyMessage(info, "请稍稍等,下次记得圈晴天就好啦~~😊😊", false, MsgType.TEXT, false)
                }

                var replyContent = chatBeforeFilter.second
                if (chatBeforeFilter.first) {
                    // 请求GPT
                    replyContent = "我是GPT回复"
                    //requestChat(event.eventMessage!!.content!!)
                }
                robotReplyMessage(info, replyContent, isAtRoBotMessage, MsgType.CARD)
            }
        }
    }

    /**
     * 机器人回复消息
     * @param [info] 信息
     * @param [replyContent] 回复内容
     */
    private fun robotReplyMessage(
        info: FeiShuWebhookRequest<ReceiveEventInfo>,
        openaiContent: String,
        isAtRoBotMessage: Boolean,
        messageType: MsgType,
        idempotent: Boolean = true
    ) {
        if (isAtRoBotMessage) {
            robot.replyMessages(ReplyMessagesRequest().apply {
                messageId = info.event!!.eventMessage!!.messageId!!
                msgType = messageType.value
                content = when (messageType) {
                    MsgType.CARD -> cardContentTemplate.message(true, info.event!!, openaiContent)
                    MsgType.TEXT -> jsonMapper().writeValueAsString(hashMapOf("text" to openaiContent))
                }
                if (idempotent) uuid = info.header!!.eventId
            })
        } else {
            robot.sendMessages(SendMessagesRequest().apply {
                receiveIdType = "open_id"
                receiveId = info.event!!.eventSender!!.senderId!!.openId
                msgType = messageType.value
                content = when (messageType) {
                    MsgType.CARD -> cardContentTemplate.message(false, info.event!!, openaiContent)
                    MsgType.TEXT -> jsonMapper().writeValueAsString(hashMapOf("text" to openaiContent))
                }
                if (idempotent) uuid = info.header!!.eventId
            })
        }
    }


    private fun chatBeforeFilter(event: ReceiveEventInfo): Pair<Boolean, String> {
        val text = jsonMapper().readTree(event.eventMessage!!.content!!)["text"]?.asText()
        return if (!StringUtils.hasText(text)) {
            Pair(false, "如果你需要晴天的帮助,可以使用文本消息与晴天进行对话~~☀️☀️️️")
        } else Pair(true, text!!)
    }

    /**
     * 请求聊天
     * @param [body] 身体
     */
    private fun requestChat(body: String) = try {
        val chat = openai.chat(body)
        chat.choices!![0].messages?.content ?: "晴天式无语!!😓😓"
    } catch (e: SocketTimeoutException) {
        "🤖️这个问题容我再想想,请等一等再来问我吧~~"
    } catch (e: ConnectTimeoutException) {
        "🤖️这个问题容我再想想,请等一等再来问我吧~~"
    } catch (e: Exception) {
        "可能今天是阴天!!☁️☁️️️"
    }
}