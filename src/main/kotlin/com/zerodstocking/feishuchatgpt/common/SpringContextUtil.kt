package com.zerodstocking.feishuchatgpt.common

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

/**
 * @author JayCHou <a href="calljaychou@qq.com">Email</a>
 * @version 2.0.0
 * @date 2023/4/25 22:22
 */
@Component
class SpringContextUtil : ApplicationContextAware {

    override fun setApplicationContext(context: ApplicationContext) {
        SpringContextUtil.context = context
    }

    companion object {
        private lateinit var context: ApplicationContext
        private fun getApplicationContext() = context

        /**
         * 通过class获取Bean
         * @param [clazz] bean class
         */
        fun <T> getBean(clazz: Class<T>) = getApplicationContext().getBean(clazz)

        /**
         * 通过name,以及Clazz返回指定的Bean
         * @param [name] bean name
         * @param [clazz] bean class
         */
        fun <T> getBean(name: String, clazz: Class<T>) = getApplicationContext().getBean(name, clazz)
    }
}