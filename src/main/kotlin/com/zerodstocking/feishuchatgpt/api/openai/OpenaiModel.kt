package com.zerodstocking.feishuchatgpt.api.openai

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.zerodstocking.feishuchatgpt.common.annotation.NoArg

/**
 * @author JayCHou <a href="calljaychou@qq.com">Email</a>
 * @version 2.0.0
 * @date 2023/4/28 00:20
 */

sealed class API(
    @JsonIgnore val request: OpenaiAPIEnum
)

@NoArg
class ModelsRequest : API(OpenaiAPIEnum.GET_MODELS)

@NoArg
data class ChatRequest(
    val model: String = "gpt-3.5-turbo-0301",
    val temperature: Float = 0.8f,
    val n: Int = 1,
    @JsonProperty("top_p")
    var topP: Float? = null,
    @JsonProperty("frequency_penalty")
    val frequencyPenalty: Float? = 0.0f,
    @JsonProperty("presence_penalty")
    val presencePenalty: Float = 0.6f,
    @JsonProperty("max_tokens")
    val maxTokens: Int = 128,
    var stop: List<String>? = null,
    var stream: Boolean? = null,
    var messages: List<MessageInfo>? = null,

    ) : API(OpenaiAPIEnum.CHAT) {
    @NoArg
    data class MessageInfo(
        val role: String = "user",
        var content: String? = null,
        var name: String? = null,
    )
}

@NoArg
data class ChatResponse(
    var id: String? = null,
    var `object`: String? = null,
    var created: String? = null,
    var model: String? = null,
    var choices: List<ChoiceInfo>? = null,
    var usage: UsageInfo? = null,
) {
    @NoArg
    data class ChoiceInfo(
        var index: Int? = null,
        var messages: ChatRequest.MessageInfo? = null,
        @JsonProperty("finish_reason")
        var finishReason: String? = null
    )

    @NoArg
    data class UsageInfo(
        @JsonProperty("prompt_tokens")
        var promptTokens: Int? = null,
        @JsonProperty("completion_tokens")
        var completionTokens: Int? = null,
        @JsonProperty("total_tokens")
        var totalTokens: Int? = null,
    )
}

@NoArg
data class ModelsInfoResponse(
    var `object`: String? = null,
    var data: List<Model>? = null
) {
    @NoArg
    data class Model(
        var id: String? = null,
        var `object`: String? = null,
        var created: String? = null,
        @JsonProperty("owned_by")
        var ownedBy: String? = null,
        var permission: List<Permission>? = null,
    ) {
        @NoArg
        data class Permission(
            var id: String? = null,
            var `object`: String? = null,
            var created: String? = null,
            @JsonProperty("allow_create_engine")
            var allowCreateEngine: Boolean? = null,
            @JsonProperty("allow_sampling")
            var allowSampling: Boolean? = null,
            @JsonProperty("allow_logprobs")
            var allowLogprobs: Boolean? = null,
            @JsonProperty("allow_search_indices")
            var allowSearchIndices: Boolean? = null,
            @JsonProperty("allow_view")
            var allowView: Boolean? = null,
            @JsonProperty("allow_fine_tuning")
            var allowFineTuning: Boolean? = null,
        )
    }
}
