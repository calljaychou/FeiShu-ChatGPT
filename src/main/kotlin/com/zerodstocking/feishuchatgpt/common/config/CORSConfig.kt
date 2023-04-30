package com.zerodstocking.feishuchatgpt.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
class CORSConfig {
    @Bean
    fun corsFilter() = CorsFilter(UrlBasedCorsConfigurationSource().apply {
        registerCorsConfiguration("/**", CorsConfiguration().apply {
            addAllowedOrigin("*")
            //是否发送Cookie信息
            allowCredentials = true
            //放行哪些原始域(请求方式)
            addAllowedMethod("*")
            //放行哪些原始域(头部信息)
            addAllowedHeader("*")
        })
    })
}