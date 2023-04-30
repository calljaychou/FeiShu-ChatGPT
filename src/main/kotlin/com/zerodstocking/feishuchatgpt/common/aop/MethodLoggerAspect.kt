package com.zerodstocking.feishuchatgpt.common.aop

import com.zerodstocking.feishuchatgpt.common.annotation.MethodLogger
import com.zerodstocking.feishuchatgpt.common.exception.jsonMapper
import com.zerodstocking.feishuchatgpt.common.logger
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component

@Aspect
@Component
class MethodLoggerAspect {

    private val log = logger()

    @Pointcut("@annotation(com.zerodstocking.feishuchatgpt.common.annotation.MethodLogger)")
    fun logPoint() {
        // logPoint
    }

    @Around("logPoint()")
    @Throws(Throwable::class)
    fun around(point: ProceedingJoinPoint): Any? {
        val methodName = point.signature.toShortString()
        val invokeParams = point.args

        val annotation = (point.signature as MethodSignature).method.getAnnotation(MethodLogger::class.java)
        val identifier = annotation.title

        val proceed: Any?
        val startTime = System.currentTimeMillis()
        return try {
            proceed = point.proceed(point.args)
            log.info(
                successLog(
                    "[$identifier][$methodName]",
                    (System.currentTimeMillis() - startTime),
                    invokeParams,
                    proceed
                )
            )
            proceed
        } catch (throwable: Throwable) {
            log.error(
                errorLog(
                    "[$identifier][$methodName]",
                    (System.currentTimeMillis() - startTime),
                    invokeParams,
                    throwable
                )
            )
            throw throwable
        }
    }

    fun successLog(methodName: String, milliSeconds: Long, inputParams: Any?, outPutParams: Any?): String =
        "调用$methodName , 耗时:$milliSeconds 毫秒; input=[${jsonMapper().writeValueAsString(inputParams)}], output=[${
            jsonMapper().writeValueAsString(
                outPutParams
            )
        }]"

    fun errorLog(methodName: String, milliSeconds: Long, inputParams: Any?, throwable: Throwable): String {
        log.error(throwable.stackTraceToString())

        return "调用$methodName 异常, 耗时:$milliSeconds 毫秒; input=[${jsonMapper().writeValueAsString(inputParams)}], error=[${
            throwable.message
        }]"
    }

}

