package com.zerodstocking.feishuchatgpt.common.config

import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import java.net.SocketOptions.TCP_NODELAY
import java.util.concurrent.TimeUnit
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ClientHttpConnector
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.client.reactive.ReactorResourceFactory
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import reactor.netty.resources.LoopResources
import reactor.netty.tcp.ProxyProvider


/**
 * @author JayCHou <a href="calljaychou@qq.com">Email</a>
 * @version 2.0.0
 * @date 2023/5/4 20:28
 */
@Configuration
class WebClientConfig {

    @Bean
    fun resourceFactory() = ReactorResourceFactory().apply {
        isUseGlobalResources = false
        connectionProvider = ConnectionProvider.create("httpClient", 50)
        loopResources = LoopResources.create("httpClient", 50, true)

    }

    @Bean
    fun webClient(): WebClient {
        val mapper = java.util.function.Function<HttpClient, HttpClient> {
            it.tcpConfiguration { tcp ->
                tcp.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000 * 6 * 5)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .doOnConnected { conn ->
                        conn.addHandlerLast(ReadTimeoutHandler(30))
                        conn.addHandlerLast(WriteTimeoutHandler(30))
                    }
                tcp.proxy { proxy ->
                    proxy.type(ProxyProvider.Proxy.HTTP)
                        .host("https://127.0.0.1")
                        .port(8080)
                }
            }
        }
        return WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(resourceFactory(), mapper))
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
    }
}