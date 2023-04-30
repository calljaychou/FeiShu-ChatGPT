package com.zerodstocking.feishuchatgpt.api.feishu

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.zerodstocking.feishuchatgpt.common.annotation.NoArg

/**
 * @author JayCHou <a href="calljaychou@qq.com">Email</a>
 * @version 2.0.0
 * @date 2023/4/19 22:58
 */
@NoArg
class RobotInfoRequest : FeiShuApi(FeiShuAPIEnum.GET_ROBOT_INFO)

@NoArg
data class RobotInfoResponse(var bot: RobotInfo? = null) : Result()

@NoArg
data class ReplyMessagesRequest(
    @JsonIgnore
    var messageId: String? = null,
    @JsonProperty("msg_type")
    var msgType: String? = null,
    var content: String? = null,
    var uuid: String? = null
) : FeiShuApi(FeiShuAPIEnum.REPLY_MESSAGES)

@NoArg
data class SendMessagesRequest(
    @JsonIgnore
    var receiveIdType: String? = null,
    @JsonProperty("receive_id")
    var receiveId: String? = null,
    @JsonProperty("msg_type")
    var msgType: String? = null,
    var content: String? = null,
    var uuid: String? = null,
) : FeiShuApi(FeiShuAPIEnum.SEND_MESSAGES)

@NoArg
data class MessagesResponse(var data: MessagesInfo? = null) : Result()

@NoArg
data class MessagesInfo(
    @JsonProperty("message_id")
    var messageId: String? = null,
    @JsonProperty("root_id")
    var rootId: String? = null,
    @JsonProperty("parent_id")
    var parentId: String? = null,
    @JsonProperty("msg_type")
    var msgType: String? = null,
    @JsonProperty("create_time")
    var createTime: String? = null,
    @JsonProperty("update_time")
    var updateTime: String? = null,
    var deleted: String? = null,
    var updated: String? = null,
    @JsonProperty("chat_id")
    var chatId: String? = null,
    var sender: SenderInfo? = null,
    var body: BodyInfo? = null,
    var mentions: List<MentionInfo>? = null,
    @JsonProperty("upper_message_id")
    var upperMessageId: String? = null
) {
    @NoArg
    data class SenderInfo(
        var id: String? = null,
        @JsonProperty("id_type")
        var idType: String? = null,
        @JsonProperty("sender_type")
        var senderType: String? = null,
        @JsonProperty("tenant_key")
        var tenantKey: String? = null
    )

    @NoArg
    data class BodyInfo(var content: String? = null)

    @NoArg
    data class MentionInfo(
        /**
         * mention key
         */
        var key: String? = null,
        /**
         * 用户 ID
         */
        var id: String? = null,
        /**
         * 用户姓名
         */
        var name: String? = null,
        /**
         * tenant key，为租户在飞书上的唯一标识，用来换取对应的tenant_access_token，也可以用作租户在应用里面的唯一标识
         */
        @JsonProperty("tenant_key")
        var tenantKey: String? = null
    )
}

@NoArg
data class RobotInfo(
    /**
     * 机器人信息
     */
    var bot: String? = null,
    /**
     * @see BotActivateStatus
     * app 当前状态
     */
    @JsonProperty("activate_status")
    var activateStatus: Int? = null,
    /**
     * app 名称
     */
    @JsonProperty("app_name")
    var appName: String? = null,
    /**
     * app 图像地址
     */
    @JsonProperty("avatar_url")
    var avatarUrl: String? = null,
    /**
     * app 的 IP 白名单地址
     */
    @JsonProperty("ip_white_list")
    var ipWhiteList: List<String?> = listOf(),
    /**
     * 机器人的open_id
     */
    @JsonProperty("open_id")
    var openId: String? = null
)

