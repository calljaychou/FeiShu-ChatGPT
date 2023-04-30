package com.zerodstocking.feishuchatgpt

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FeishuChatGPTApplication

val log: Logger = LoggerFactory.getLogger(FeishuChatGPTApplication::class.java)

fun main(args: Array<String>) {
    runApplication<FeishuChatGPTApplication>(*args)

    log.info(
        "\n" + """
          ____ ______   _____ ________ ____     ____ __   ___ _____    __      _  _____   
         / __ (_  __ \ / ____(___  ___/ __ \   / ___() ) / __(_   _)  /  \    / )/ ___ \  
        ( (  ) )) ) \ ( (___     ) ) / /  \ \ / /   ( (_/ /    | |   / /\ \  / // /   \_) 
        ( (  ) ( (   ) \___ \   ( ( ( ()  () ( (    ()   (     | |   ) ) ) ) ) ( (  ____  
        ( (  ) )) )  ) )   ) )   ) )( ()  () ( (    () /\ \    | |  ( ( ( ( ( (( ( (__  ) 
        ( (__) / /__/ /___/ /   ( (  \ \__/ / \ \___( (  \ \  _| |__/ /  \ \/ / \ \__/ /  
         \____(______//____/    /__\  \____/   \____()_)  \_\/_____(_/    \__/   \____/   
                                                                                         
    """.trimIndent()
    )
}
