package com.zerodstocking.feishuchatgpt.api.feishu.robot

import com.fasterxml.jackson.core.type.TypeReference
import com.zerodstocking.feishuchatgpt.api.feishu.FeiShuInvoke
import com.zerodstocking.feishuchatgpt.api.feishu.MessagesInfo
import com.zerodstocking.feishuchatgpt.api.feishu.MessagesResponse
import com.zerodstocking.feishuchatgpt.api.feishu.ReplyMessagesRequest
import com.zerodstocking.feishuchatgpt.api.feishu.ResultCode
import com.zerodstocking.feishuchatgpt.api.feishu.RobotInfo
import com.zerodstocking.feishuchatgpt.api.feishu.RobotInfoRequest
import com.zerodstocking.feishuchatgpt.api.feishu.RobotInfoResponse
import com.zerodstocking.feishuchatgpt.api.feishu.SendMessagesRequest
import com.zerodstocking.feishuchatgpt.common.exception.BizException
import org.springframework.stereotype.Service

/**
 * @author JayCHou <a href="calljaychou@qq.com">Email</a>
 * @version 2.0.0
 * @date 2023/4/19 22:53
 */
@Service

class RobotAPI(private val feishu: FeiShuInvoke) {

    /**
     * 获取机器人信息
     * @return [RobotInfo]
     */
    fun getRobot(): RobotInfo {
        val valueType = object : TypeReference<RobotInfoResponse>() {}
        val ret = feishu.sendRequest(RobotInfoRequest(), valueType)
        return if (ResultCode.SUCCESS.code == ret.code) {
            ret.bot!!
        } else {
            throw BizException(BizException.BUSINESS_FAILED, "获取获取机器人信息失败, 错误码：${ret.msg}")
        }
    }

    /**
     * 发送消息
     * @param [request] 请求
     * @return [MessagesInfo]
     */
    fun sendMessages(request: SendMessagesRequest): MessagesInfo {
        val valueType = object : TypeReference<MessagesResponse>() {}
        request.request.url = request.request.url.replace("$1", request.receiveIdType!!)
        val ret = feishu.sendRequest(request, valueType)
        return if (ResultCode.SUCCESS.code == ret.code) {
            ret.data!!
        } else {
            throw BizException(BizException.BUSINESS_FAILED, "发送消息失败, 错误码：${ret.msg}")
        }
    }

    /**
     * 回复消息
     * @param [request] 请求
     * @return [MessagesInfo]
     */
    fun replyMessages(request: ReplyMessagesRequest): MessagesInfo {
        val valueType = object : TypeReference<MessagesResponse>() {}
        request.request.url = request.request.url.replace("$1", request.messageId!!)
        val ret = feishu.sendRequest(request, valueType)
        return if (ResultCode.SUCCESS.code == ret.code) {
            ret.data!!
        } else {
            throw BizException(BizException.BUSINESS_FAILED, "回复消息失败, 错误码：${ret.msg}")
        }
    }
}