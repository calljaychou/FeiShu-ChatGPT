package com.zerodstocking.feishuchatgpt.common.constant

/**
 * @author JayCHou <a href="calljaychou@qq.com">Email</a>
 * @version 2.0.0
 * @date 2022/12/5 14:40
 */

enum class ChatType(val value: String) {
    P2P("p2p"),
    GROUP("group")
}

enum class MsgType(val value: String) {
    TEXT("text"),
    CARD("interactive"),

}