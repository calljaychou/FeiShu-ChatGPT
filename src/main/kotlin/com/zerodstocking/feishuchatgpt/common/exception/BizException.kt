package com.zerodstocking.feishuchatgpt.common.exception


class BizException : RuntimeException {
    var code: Int

    constructor(code: Int, message: String) : super(message) {
        this.code = code
    }

    constructor(code: Int, message: String, cause: Throwable) : super(message, cause) {
        this.code = code
    }

    companion object {
        const val BUSINESS_FAILED = 400
        const val SYSTEM_FAILED = 500
    }
}