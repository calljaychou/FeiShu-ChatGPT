package com.zerodstocking.feishuchatgpt.common.config

import java.nio.charset.Charset
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.web.client.RestTemplate


/**
 * @author JayCHou <a href="calljaychou@qq.com">Email</a>
 * @version 2.0.0
 * @date 2023/4/12 12:17
 */
@Configuration
class RestTemplateConfig {

    @Bean
    fun restTemplate(factory: ClientHttpRequestFactory): RestTemplate = RestTemplate(factory).apply {
        messageConverters[1] = StringHttpMessageConverter(Charset.forName("UTF-8"))
    }

    @Bean
    fun simpleClientHttpRequestFactory() = SimpleClientHttpRequestFactory().apply {
        setReadTimeout(1000 * 6 * 5)
        setConnectTimeout(1000 * 6 * 5)

    }

}