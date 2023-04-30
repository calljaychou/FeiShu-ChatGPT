package com.zerodstocking.feishuchatgpt.service

import com.zerodstocking.feishuchatgpt.common.SpringContextUtil
import com.zerodstocking.feishuchatgpt.common.exception.BizException

/**
 * @author JayCHou <a href="calljaychou@qq.com">Email</a>
 * @version 2.0.0
 * @date 2023/4/25 21:44
 */
interface EventService {

    fun handler(body: String)

    companion object {
        /**
         * 创建实例
         * @param [eventType] 事件类型
         */
        fun instance(eventType: String) = when (eventType) {
            "im.chat.member.bot.added_v1" -> SpringContextUtil.getBean(BotAddedEventService::class.java)
            "im.message.receive_v1" -> SpringContextUtil.getBean(ReceiveEventService::class.java)
            else -> throw BizException(BizException.BUSINESS_FAILED, "未定义的事件类型")
        }
    }


}