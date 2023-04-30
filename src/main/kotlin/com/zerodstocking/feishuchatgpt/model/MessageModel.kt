package com.zerodstocking.feishuchatgpt.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.zerodstocking.feishuchatgpt.common.annotation.NoArg

/**
 * @author JayCHou <a href="calljaychou@qq.com">Email</a>
 * @version 2.0.0
 * @date 2023/4/22 23:36
 */


@NoArg
open class URLVerification(
    var challenge: String? = null,
    var token: String? = null,
    var type: String? = null
)

@NoArg
data class FeiShuWebhookRequest<T>(
    /**
     * 事件模式
     */
    var schema: String? = null,
    /**
     * 事件头
     */
    var header: EventHeaderInfo? = null,

    var event: T? = null
) : URLVerification() {
    @NoArg
    data class EventHeaderInfo(
        /**
         * 事件 ID
         */
        @JsonProperty("event_id")
        val eventId: String? = null,
        /**
         * 事件类型
         */
        @JsonProperty("event_type")
        val eventType: String? = null,
        /**
         *  事件创建时间戳（单位：毫秒）
         */
        @JsonProperty("create_time")
        val createTime: String? = null,
        /**
         * 事件 Token
         */
        val token: String? = null,
        /**
         * 应用 ID
         */
        @JsonProperty("app_id")
        val appId: String? = null,
        /**
         * 租户 Key
         */
        @JsonProperty("tenant_key")
        val tenantKey: String? = null
    )
}

@NoArg
data class ReceiveEventInfo(
    /**
     * 事件的发送者
     */
    @JsonProperty("sender")
    var eventSender: EventSender? = null,
    /**
     * 事件中包含的消息内容
     */
    @JsonProperty("message")
    var eventMessage: EventMessage? = null
) {
    @NoArg
    data class EventSender(
        /**
         * 用户 ID
         */
        @JsonProperty("sender_id")
        var senderId: UserId? = null,
        /**
         * 消息发送者类型。目前只支持用户(user)发送的消息
         */
        @JsonProperty("sender_type")
        var senderType: String? = null,
        /**
         * tenant key，为租户在飞书上的唯一标识，用来换取对应的tenant_access_token，也可以用作租户在应用里面的唯一标识
         */
        @JsonProperty("tenant_key")
        var tenantKey: String? = null
    ) {
        @NoArg
        data class UserId(
            /**
             * 用户的 union id
             */
            @JsonProperty("union_id")
            var unionId: String? = null,
            /**
             * 用户的 user id
             */
            @JsonProperty("user_id")
            var userId: String? = null,
            /**
             * 用户的 open id
             */
            @JsonProperty("open_id")
            var openId: String? = null
        )
    }

    @NoArg
    data class EventMessage(
        /**
         *
        消息的open_message_id
         */
        @JsonProperty("message_id")
        var messageId: String? = null,
        /**
         * 根消息id，用于回复消息场景
         */
        @JsonProperty("root_id")
        var rootId: String? = null,
        /**
         * 父消息的id，用于回复消息场景
         */
        @JsonProperty("parent_id")
        var parentId: String? = null,
        /**
         * 消息发送时间（毫秒）
         */
        @JsonProperty("create_time")
        var createTime: String? = null,
        /**
         * 消息所在的群组 ID
         */
        @JsonProperty("chat_id")
        var chatId: String? = null,
        /**
         * 消息所在的群组类型,p2p：单聊,group： 群组
         */
        @JsonProperty("chat_type")
        var chatType: String? = null,
        /**
         * 消息类型
         */
        @JsonProperty("message_type")
        var messageType: String? = null,
        /**
         * 消息内容, json 格式
         */
        var content: String? = null,
        /**
         * 被提及用户的信息
         */
        var mentions: List<MentionEvent>? = null,
    ) {
        @NoArg
        data class MentionEvent(
            /**
             * mention key
             */
            var key: String? = null,
            /**
             * 用户 ID
             */
            var id: EventSender.UserId? = null,
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
}

@NoArg
data class BotAdderEventInfo(
    /**
     * 群组 ID，详情参见
     */
    @JsonProperty("chat_id")
    var chatId:String? = null,
    /**
     * 用户 ID
     */
    @JsonProperty("operator_id")
    var operatorId:ReceiveEventInfo.EventSender.UserId? = null,
    /**
     * 是否是外部群
     */
    var external:Boolean? = null,
    /**
     * 操作者的租户Key，为租户在飞书上的唯一标识，用来换取对应的tenant_access_token，也可以用作租户在应用中的唯一标识
     */
    @JsonProperty("operator_tenant_key")
    var operatorTenantKey:String? = null,
    /**
     * 群名称
     */
    var name:String? = null,
    /**
     * 群国际化名称
     */
    @JsonProperty("i18n_names")
    var i18nNames: I18nNames? = null
){
    @NoArg
    data class I18nNames(
        /**
         * 中文名
         */
        @JsonProperty("zh_cn")
        var zhCn:String? = null,
        /**
         * 英文名
         */
        @JsonProperty("en_us")
        var enUs:String? = null,
        /**
         * 日文名
         */
        @JsonProperty("ja_jp")
        var jaJp:String? = null,
    )
}

