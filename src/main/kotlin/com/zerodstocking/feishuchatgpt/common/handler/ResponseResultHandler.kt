package com.zerodstocking.feishuchatgpt.common.handler

import org.springframework.core.MethodParameter
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice


/**
 * @author JayCHou <a href="calljaychou@qq.com">Email</a>
 * @version 2.0.0
 * @date 2023/4/12 14:24
 */
@ControllerAdvice
class ResponseResultHandler : ResponseBodyAdvice<Any> {


    override fun supports(methodParameter: MethodParameter, aClass: Class<out HttpMessageConverter<*>>): Boolean {
        return methodParameter.declaringClass.isAnnotationPresent(RestController::class.java)
    }

    override fun beforeBodyWrite(
        p0: Any?,
        p1: MethodParameter,
        p2: MediaType,
        p3: Class<out HttpMessageConverter<*>>,
        p4: ServerHttpRequest,
        p5: ServerHttpResponse
    ) = when (p0) {
        is Map<*, *> -> p0
        is String -> p0
        else -> hashMapOf<String, Any>("code" to HttpStatus.OK, "message" to "success").also {
            if (p0 != null) it["data"] = p0
        }
    }
}