package com.zerodstocking.feishuchatgpt.mq

import com.zerodstocking.feishuchatgpt.common.exception.jsonMapper
import com.zerodstocking.feishuchatgpt.common.logger
import com.zerodstocking.feishuchatgpt.model.FeiShuWebhookRequest
import com.zerodstocking.feishuchatgpt.service.EventService
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service

/**
 * @author JayCHou <a href="calljaychou@qq.com">Email</a>
 * @version 2.0.0
 * @date 2023/4/25 11:05
 */
@Service
class LocalQueueFeiShuMessageProducer : FeiShuMessageProducer {

    companion object {
        private const val MAX_VALUE = 2000
    }

    private val queue: BlockingQueue<String>

    init {
        queue = LinkedBlockingQueue(MAX_VALUE)
        createListener()
    }

    override fun production(request: String) {
        queue.put(request)
        logger().info("飞书消息发送到ISV队列,参数:{}", request)
    }

    @Suppress("unused")
    private fun createListener() = GlobalScope.launch {
        while (true) {
            queue.take().run {
                kotlin.runCatching {
                    logger().info("消费队列元素,element:{}", this)
                    val readValue = jsonMapper().readValue(this, FeiShuWebhookRequest::class.java)
                    val instance = EventService.instance(readValue.header!!.eventType!!)
                    instance.handler(this)
                }.onFailure {
                    logger().error("消费队列元素异常,element:{}", this, it)
                }
            }
        }
    }


}