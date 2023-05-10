package com.zerodstocking.feishuchatgpt.controller

import com.zerodstocking.feishuchatgpt.api.feishu.robot.RobotAPI
import com.zerodstocking.feishuchatgpt.api.openai.OpenaiAPI
import com.zerodstocking.feishuchatgpt.common.exception.jsonMapper
import com.zerodstocking.feishuchatgpt.common.logger
import com.zerodstocking.feishuchatgpt.service.ReceiveEventService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


/**
 * @author JayCHou <a href="calljaychou@qq.com">Email</a>
 * @version 2.0.0
 * @date 2023/4/19 13:13
 */
@RestController
@RequestMapping("/test")
class TestController {

    @Autowired
    lateinit var openaiAPI: OpenaiAPI

    @Autowired
    lateinit var robotAPI: RobotAPI

    @Autowired
    lateinit var receiveEventService: ReceiveEventService

    val log = logger()

    @GetMapping("/ping")
    fun test() = "pong"

    @GetMapping("/token")
    fun token() {
        val robot = robotAPI.getRobot()
        log.info("robot:{}", jsonMapper().writeValueAsString(robot))
    }

    @GetMapping("/chat")
    fun chat() {
        val chat = openaiAPI.chat("给我一份300字的介绍,我叫jaychou")
        logger().info("openai:{}", jsonMapper().writeValueAsString(chat))
    }

    @GetMapping("/chatStream")
    fun chatStream() {
        val chat = openaiAPI.chatStream("给我一份300字的介绍,我叫jaychou")
        chat.subscribe { println(it) }
    }



}