package com.zerodstocking.feishuchatgpt.common

import com.zerodstocking.feishuchatgpt.common.exception.BizException
import java.util.concurrent.TimeUnit
import org.redisson.api.RedissonClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.MDC

inline fun <reified T> T.logger(): Logger {
    return LoggerFactory.getLogger(T::class.java)
}


inline fun <reified T> T.runWithLock(redisson: RedissonClient, lockKey: String, block: () -> Unit) {
    val lock = redisson.getLock(lockKey)
    if (!lock.tryLock(0, TimeUnit.SECONDS)) {
        logger().warn("任务已经在执行中，请稍后再试")
        return
    }
    try {
        block.invoke()
    } catch (e: Exception) {
        logger().error("任务处理异常:  ${e.stackTraceToString()}")
    } finally {
        lock.unlock()
    }
}

inline fun <reified T> T.runWithPairLockWithEx(
    redisson: RedissonClient,
    firstLockKey: String,
    secondLockKey: String,
    block: () -> Unit
) {
    val firstLock = redisson.getLock(firstLockKey)
    val secondLoc = redisson.getLock(secondLockKey)
    if (!firstLock.tryLock(0, TimeUnit.SECONDS) || !secondLoc.tryLock(0, TimeUnit.SECONDS)) {
        logger().warn("任务已经在执行中，请稍后再试")
        return
    }
    try {
        block.invoke()
    } catch (ex: Exception) {
        throw BizException(BizException.BUSINESS_FAILED, ex.message.toString())
    } finally {
        if (firstLock.isLocked && firstLock.isHeldByCurrentThread) firstLock.unlock()
        if (secondLoc.isLocked && secondLoc.isHeldByCurrentThread) secondLoc.unlock()
    }
}

inline fun <reified T> T.runWithLockWithEx(redisson: RedissonClient, lockKey: String, block: () -> Unit) {
    val lock = redisson.getLock(lockKey)
    if (!lock.tryLock(0, TimeUnit.SECONDS)) {
        logger().warn("任务已经在执行中，请稍后再试")
        return
    }
    try {
        block.invoke()
    } catch (ex: Exception) {
        throw BizException(BizException.BUSINESS_FAILED, ex.message.toString())
    } finally {
        lock.unlock()
    }
}

inline fun <reified T> T.runWithLockExWithEx(redisson: RedissonClient, lockKey: String, block: () -> Unit) {
    val lock = redisson.getLock(lockKey)
    if (!lock.tryLock(0, TimeUnit.SECONDS)) {
        logger().warn("任务已经在执行中，请稍后再试")
        throw BizException(BizException.BUSINESS_FAILED, "任务已经在执行中，请稍后再试")
    }
    try {
        block.invoke()
    } catch (ex: Exception) {
        throw BizException(BizException.BUSINESS_FAILED, ex.message.toString())
    } finally {
        lock.unlock()
    }
}

fun getTraceId(): String? = MDC.get("TraceId")

fun setTraceId(traceId: String?) = MDC.put("TraceId", traceId)

fun asyncTraceRun(runnable: Runnable) {
    asyncTraceRun(getTraceId(), runnable)
}

fun asyncTraceRun(traceId: String?, runnable: Runnable) {
    if (traceId == null) {
        runnable.run()
        return
    }
    val originalTraceId = getTraceId()
    setTraceId(traceId)
    try {
        runnable.run()
    } finally {
        setTraceId(originalTraceId)
    }
}


fun responseSuccess() = hashMapOf("code" to 200, "message" to "success")