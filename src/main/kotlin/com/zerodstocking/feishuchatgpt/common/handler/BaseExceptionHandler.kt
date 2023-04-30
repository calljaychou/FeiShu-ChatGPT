package com.zerodstocking.feishuchatgpt.common.handler

import com.zerodstocking.feishuchatgpt.common.exception.BizException
import com.zerodstocking.feishuchatgpt.common.exception.FeiShuURLVerifyException
import com.zerodstocking.feishuchatgpt.common.logger
import javax.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

@ControllerAdvice
class BaseExceptionHandler {

    val log = logger()

    @ResponseBody
    @ExceptionHandler(value = [FeiShuURLVerifyException::class])
    fun feiShuURLVerifyExceptionHandler(ex: Exception): ResponseEntity<Any> {
        log.info("飞书URL配置校验处理:{}", ex.message)
        return ResponseEntity.status(HttpStatus.OK).body(hashMapOf("challenge" to ex.message))
    }

    @ResponseBody
    @ExceptionHandler(value = [Exception::class])
    fun exceptionHandler(request: HttpServletRequest, ex: Exception): ResponseEntity<Any> {
        log.error("服务器内部错误: ", ex)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(hashMapOf("code" to 500, "message" to ex.message))
    }

    @ResponseBody
    @ExceptionHandler(value = [BizException::class])
    fun bizExceptionHandler(request: HttpServletRequest, ex: Exception): ResponseEntity<Any> {
        log.error("业务内部错误: ", ex)
        return ResponseEntity.status(HttpStatus.OK).body(hashMapOf("code" to 500, "message" to ex.message))
    }

}