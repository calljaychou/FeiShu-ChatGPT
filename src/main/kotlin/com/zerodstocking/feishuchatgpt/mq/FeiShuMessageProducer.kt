package com.zerodstocking.feishuchatgpt.mq

/**
 * @author JayCHou <a href="calljaychou@qq.com">Email</a>
 * @version 2.0.0
 * @date 2023/4/25 10:58
 */
interface FeiShuMessageProducer {
    fun production(request: String)
}