package com.zerodstocking.feishuchatgpt.common.config

import com.zerodstocking.feishuchatgpt.common.FeiShuEncryptUtil
import com.zerodstocking.feishuchatgpt.common.annotation.FeiShuMessageRequest
import com.zerodstocking.feishuchatgpt.common.exception.FeiShuURLVerifyException
import com.zerodstocking.feishuchatgpt.common.exception.jsonMapper
import com.zerodstocking.feishuchatgpt.common.logger
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.StringWriter
import javax.servlet.ReadListener
import javax.servlet.ServletInputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * @author JayCHou <a href="calljaychou@qq.com">Email</a>
 * @version 2.0.0
 * @date 2023/4/24 09:26
 */
@Configuration
class WebMvcConfig : WebMvcConfigurer {

    @Autowired
    lateinit var feiShuEncryptUtil: FeiShuEncryptUtil

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(feiShuMessageParamsResolver())
        super.addArgumentResolvers(resolvers)
    }

    fun feiShuMessageParamsResolver() = object : HandlerMethodArgumentResolver {

        override fun supportsParameter(parameter: MethodParameter) =
            parameter.hasParameterAnnotation(FeiShuMessageRequest::class.java)

        override fun resolveArgument(
            p0: MethodParameter, p1: ModelAndViewContainer?, p2: NativeWebRequest, p3: WebDataBinderFactory?
        ): Any? {
            val request = checkNotNull(p2.getNativeRequest(HttpServletRequest::class.java)) {
                throw IllegalStateException("Current request is not of type HttpServletRequest:{$p2}")
            }
            val customHttpServletRequestWrapper = CustomHttpServletRequestWrapper(request)
            val body = jsonMapper().readTree(customHttpServletRequestWrapper.body)?.run {
                val decrypt = feiShuEncryptUtil.decrypt(this["encrypt"].asText())
                logger().info("接收飞书Webhook请求,request:{}", jsonMapper().writeValueAsString(decrypt))
                val readTree = jsonMapper().readTree(decrypt)
                Pair(readTree["challenge"]?.asText(), decrypt)
            }
            if (body?.first != null) {
                throw FeiShuURLVerifyException(body.first!!)
            }
            return body?.second
        }
    }

    /**
     * 自定义http servlet请求包装
     * @constructor 创建[CustomHttpServletRequestWrapper]
     * @param [request] 请求
     */
    class CustomHttpServletRequestWrapper(request: HttpServletRequest) : HttpServletRequestWrapper(request) {
        val body: ByteArray

        init {
            val reader = request.reader
            StringWriter().use { writer ->
                var read: Int
                @Suppress("all") val buf = CharArray(1024 * 8)
                while (reader.read(buf).also { read = it } != -1) {
                    writer.write(buf, 0, read)
                }
                this.body = writer.buffer.toString().toByteArray()
            }
        }

        @Throws(IOException::class)
        override fun getInputStream(): ServletInputStream {
            val byteArrayInputStream = ByteArrayInputStream(body)
            return object : ServletInputStream() {
                override fun read() = byteArrayInputStream.read()
                override fun isFinished() = false
                override fun isReady() = false
                override fun setReadListener(readListener: ReadListener) {
                    // empty method
                }
            }
        }

    }


}